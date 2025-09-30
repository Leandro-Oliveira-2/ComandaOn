package com.lanchonete.lanchon.models.item.service;

import com.lanchonete.lanchon.exception.domain.InvalidPayloadException;
import com.lanchonete.lanchon.exception.domain.ItemNotFoundException;
import com.lanchonete.lanchon.exception.domain.OrderNotFoundException;
import com.lanchonete.lanchon.exception.domain.ProductNotFoundException;
import com.lanchonete.lanchon.models.item.dto.CreateItem;
import com.lanchonete.lanchon.models.item.dto.ItemDTO;
import com.lanchonete.lanchon.models.item.dto.UpdateItem;
import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.item.repository.ItemRepository;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.product.entity.Product;
import com.lanchonete.lanchon.models.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public ItemDTO addItemToOrder(Long orderId, CreateItem dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Item item = buildItem(order, dto);
        Item saved = itemRepository.save(item);

        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }
        order.getItems().add(saved);
        recalculateTotals(order);

        return toDto(saved);
    }

    @Transactional
    public ItemDTO updateItem(Long orderId, Long itemId, UpdateItem dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Item item = itemRepository.findById(itemId)
                .filter(existing -> existing.getOrder() != null && existing.getOrder().getId().equals(order.getId()))
                .orElseThrow(() -> new ItemNotFoundException(itemId, orderId));

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

    @Transactional
    public void removeItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Item item = itemRepository.findById(itemId)
                .filter(existing -> existing.getOrder() != null && existing.getOrder().getId().equals(order.getId()))
                .orElseThrow(() -> new ItemNotFoundException(itemId, orderId));

        if (order.getItems() != null) {
            order.getItems().removeIf(existing -> existing.getId().equals(itemId));
        }

        itemRepository.delete(item);
        recalculateTotals(order);
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findAllByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return order.getItems().stream()
                .map(ItemService::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findAll() {
        return itemRepository.findAll().stream()
                .map(ItemService::toDto)
                .toList();
    }

    @Transactional
    public Item buildItem(Order order, CreateItem dto) {
        if (dto.productId() == null) {
            throw new InvalidPayloadException("Produto eh obrigatorio");
        }

        Product product = productRepository.findById(Math.toIntExact(dto.productId()))
                .orElseThrow(() -> new ProductNotFoundException(dto.productId()));

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
            throw new InvalidPayloadException("Nome do item eh obrigatorio");
        }
        if (unitPrice == null) {
            throw new InvalidPayloadException("Preco do item eh obrigatorio");
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

    @Transactional(readOnly = true)
    public ItemDTO getItem(Long orderId, Long itemId) {
        return itemRepository.findById(itemId)
                .filter(existing -> existing.getOrder() != null && existing.getOrder().getId().equals(orderId))
                .map(ItemService::toDto)
                .orElseThrow(() -> new ItemNotFoundException(itemId, orderId));
    }

}
