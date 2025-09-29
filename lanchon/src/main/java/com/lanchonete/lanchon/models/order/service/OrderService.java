package com.lanchonete.lanchon.models.order.service;

import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.item.repository.ItemRepository;
import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponse;
import com.lanchonete.lanchon.models.order.dto.UpdateOrder;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public static Order toEntity(CreateOrder dto, User user, List<Item> items) {
        Order order = new Order();
        order.setStatus(dto.status());
        order.setSubtotal(dto.subtotal());
        order.setTotal(dto.total());
        order.setUser(user);
        order.setItems(items);

        if (items != null) {
            items.forEach(item -> item.setOrder(order));
        }

        return order;
    }

    public static OrderResponse toResponse(Order order) {
        List<Long> itemIds = order.getItems() == null ? List.of() :
                order.getItems().stream().map(Item::getId).toList();

        Long userId = order.getUser() != null ? order.getUser().getId() : null;

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getSubtotal(),
                order.getTotal(),
                userId,
                itemIds,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    @Transactional
    public OrderResponse createOrder(CreateOrder order) {
        User user = userRepository.findById(order.userId())
                .orElseThrow(() -> {
                    System.out.println("Usuario nao encontrado: id=" + order.userId());
                    return new RuntimeException("Usuario nao encontrado");
                });

        if (order.itemIds() == null || order.itemIds().isEmpty()) {
            System.out.println("Nao deu certo nao: pedido sem itens");
            throw new RuntimeException("Pedido deve conter pelo menos 1 item.");
        }

        List<Item> items = itemRepository.findAllById(order.itemIds());

        var foundIds = new HashSet<>(items.stream().map(Item::getId).toList());
        var missing = order.itemIds().stream().filter(id -> !foundIds.contains(id)).toList();
        if (!missing.isEmpty()) {
            System.out.println("Nao deu certo nao: itens nao encontrados " + missing);
            throw new RuntimeException("Itens nao encontrados: " + missing);
        }

        Order orderCreate = toEntity(order, user, items);
        Order saved = orderRepository.save(orderCreate);
        return toResponse(saved);
    }

    @Transactional
    public OrderResponse updateOrder(Long orderId, UpdateOrder update) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    System.out.println("Pedido nao encontrado: id=" + orderId);
                    return new RuntimeException("Pedido nao encontrado");
                });

        if (update.status() != null) {
            existing.setStatus(update.status());
        }
        if (update.subtotal() != null) {
            existing.setSubtotal(update.subtotal());
        }
        if (update.total() != null) {
            existing.setTotal(update.total());
        }

        if (update.userId() != null && (existing.getUser() == null || !update.userId().equals(existing.getUser().getId()))) {
            Long userId = update.userId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        System.out.println("Usuario nao encontrado: id=" + userId);
                        return new RuntimeException("Usuario nao encontrado");
                    });
            existing.setUser(user);
        }

        if (update.itemIds() != null) {
            if (update.itemIds().isEmpty()) {
                System.out.println("Nao deu certo nao: pedido sem itens");
                throw new RuntimeException("Pedido deve conter pelo menos 1 item.");
            }

            List<Item> items = itemRepository.findAllById(update.itemIds());
            var foundIds = new HashSet<>(items.stream().map(Item::getId).toList());
            var missing = update.itemIds().stream().filter(id -> !foundIds.contains(id)).toList();
            if (!missing.isEmpty()) {
                System.out.println("Nao deu certo nao: itens nao encontrados " + missing);
                throw new RuntimeException("Itens nao encontrados: " + missing);
            }

            items.forEach(item -> item.setOrder(existing));
            existing.setItems(items);
        } else if (existing.getItems() != null) {
            existing.getItems().forEach(item -> item.setOrder(existing));
        }

        Order saved = orderRepository.save(existing);
        return toResponse(saved);
    }

    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderService::toResponse)
                .toList();
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order nao encontrado");
        }
        orderRepository.deleteById(id);
    }
}