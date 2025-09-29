package com.lanchonete.lanchon.models.order.service;

import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.item.repository.ItemRepository;
import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponse;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        Order o = new Order();
        o.setStatus(dto.status());
        o.setSubtotal(dto.subtotal());
        o.setTotal(dto.total());
        o.setUser(user);
        o.setItems(items);

        // mantém consistência do lado Item (se bidirecional)
        if (items != null) {
            items.forEach(i -> i.setOrder(o));
        }

        return o;
    }

    // Mapper: Entidade -> DTO de resposta
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
        // 1) Carregar usuário
        User user = userRepository.findById(order.userId())
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado: id=" + order.userId());
                    return new RuntimeException("Usuário não encontrado");
                });

        // 2) Verificar lista de itens
        if (order.itemIds() == null || order.itemIds().isEmpty()) {
            System.out.println("Não deu certo não: pedido sem itens");
            throw new RuntimeException("Pedido deve conter pelo menos 1 item.");
        }

        List<Item> items = itemRepository.findAllById(order.itemIds());

        // 3) Validar se todos os itens existem
        var foundIds = new HashSet<>(items.stream().map(Item::getId).toList());
        var missing = order.itemIds().stream().filter(id -> !foundIds.contains(id)).toList();
        if (!missing.isEmpty()) {
            System.out.println("Não deu certo não: itens não encontrados " + missing);
            throw new RuntimeException("Itens não encontrados: " + missing);
        }

        // 4) Montar entidade
        Order orderCreate = toEntity(order, user, items);

        // 5) Salvar
        Order saved = orderRepository.save(orderCreate);

        // 6) Retornar DTO de resposta
        return toResponse(saved);
    }

    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderService::toResponse)
                .toList();
    }

    public void deleteOrder(Long id){
        Optional<Order> orderFind = Optional.ofNullable(orderRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Order não encontrado");
        }));
        orderRepository.deleteById(Math.toIntExact(id));
    }

}
