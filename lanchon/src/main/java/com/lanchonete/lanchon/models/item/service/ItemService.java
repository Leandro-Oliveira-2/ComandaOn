package com.lanchonete.lanchon.models.item.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanchonete.lanchon.models.item.dto.CreateItem;
import com.lanchonete.lanchon.models.item.dto.ItemDTO;
import com.lanchonete.lanchon.models.item.dto.UpdateItem;
import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.item.repository.ItemRepository;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.product.entity.Product;
import com.lanchonete.lanchon.models.product.repository.ProductRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ItemService(ItemRepository itemRepository,
                       OrderRepository orderRepository,
                       ProductRepository productRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Cria e associa um item a um pedido existente, conforme o contrato do README (/orders/{id}/items).
     */
    @Transactional
    public ItemDTO addItemToOrder(Long orderId, CreateItem dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado: " + orderId));

        Item item = buildItem(order, dto);
        Item saved = itemRepository.save(item);

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }
        order.getItems().add(saved);
        recalculateTotals(order);

        return toDto(saved);
    }

    /**
     * Atualiza campos do item. Usado pelo endpoint PATCH /orders/{orderId}/items/{itemId}.
     */
    @Transactional
    public ItemDTO updateItem(Long orderId, Long itemId, UpdateItem dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado: " + orderId));

        Item item = itemRepository.findById(itemId)
                .filter(existing -> existing.getOrder() != null && existing.getOrder().getId().equals(order.getId()))
                .orElseThrow(() -> new RuntimeException("Item nao encontrado no pedido informado"));

        if (dto.nameSnapshot() != null) {
            item.setNameSnapshot(dto.nameSnapshot());
        }
        if (dto.unitPrice() != null) {
            item.setUnitPrice(dto.unitPrice());
        }
        if (dto.quantity() != null) {
            item.setQuantity(dto.quantity());
        }
        if (dto.notes() != null) {
            item.setNotes(dto.notes());
        }

        Item saved = itemRepository.save(item);
        recalculateTotals(order);
        return toDto(saved);
    }

    /**
     * Remove item de um pedido conforme DELETE /orders/{orderId}/items/{itemId}.
     */
    @Transactional
    public void removeItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado: " + orderId));

        Item item = itemRepository.findById(itemId)
                .filter(existing -> existing.getOrder() != null && existing.getOrder().getId().equals(order.getId()))
                .orElseThrow(() -> new RuntimeException("Item nao encontrado no pedido informado"));

        if (order.getItems() != null) {
            order.getItems().removeIf(existing -> existing.getId().equals(itemId));
        }

        itemRepository.delete(item);
    }

    public List<ItemDTO> findAllByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado: " + orderId));

        return order.getItems().stream()
                .map(ItemService::toDto)
                .toList();
    }



    public List<ItemDTO> findAll() {
        return itemRepository.findAll().stream()
                .map(ItemService::toDto)
                .toList();
    }

    /**
     * Utilitario compartilhado com OrderService para materializar itens a partir do DTO na criacao do pedido.
     */
    @Transactional
    public Item buildItem(Order order, CreateItem dto) {
        if (dto.productId() == null) {
            throw new RuntimeException("Produto obrigatorio");
        }

        Product product = productRepository.findById(Math.toIntExact(dto.productId()))
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: " + dto.productId()));

        String snapshotName = (dto.nameSnapshot() != null && !dto.nameSnapshot().isBlank())
                ? dto.nameSnapshot()
                : product.getName();

        BigDecimal unitPrice = dto.unitPrice() != null
                ? dto.unitPrice()
                : BigDecimal.valueOf(product.getPrice());

        Integer requestedQuantity = dto.quantity();
        int quantity = requestedQuantity != null ? requestedQuantity : 1;

        String notes = dto.notes() != null ? dto.notes() : "";

        if (snapshotName == null || snapshotName.isBlank()) {
            throw new RuntimeException("Nome do item obrigatorio");
        }
        if (unitPrice == null) {
            throw new RuntimeException("Preco do item obrigatorio");
        }

        return new Item(order, product, snapshotName, unitPrice, quantity, notes);
    }

    private void recalculateTotals(Order order) {
        if (order == null) {
            return;
        }
        BigDecimal subtotal = order.getItems() == null ? BigDecimal.ZERO :
                order.getItems().stream()
                        .map(Item::getLineTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setSubtotal(subtotal);
        order.setTotal(subtotal);
    }

    public static ItemDTO toDto(Item item) {
        Long productId = item.getProduct() != null ? Long.valueOf(item.getProduct().getId()) : null;
        return new ItemDTO(
                item.getId(),
                productId,
                item.getNameSnapshot(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal(),
                item.getNotes()
        );
    }

    public ItemDTO getItem(Long orderId, Long itemId) {
        return itemRepository.findById(itemId)
                .filter(existing -> existing.getOrder() != null && existing.getOrder().getId().equals(orderId))
                .map(ItemService::toDto)
                .orElseThrow(() -> new RuntimeException("Item nao encontrado no pedido informado"));
    }

}
