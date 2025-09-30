package com.lanchonete.lanchon.models.order.service;

import com.lanchonete.lanchon.exception.domain.UserNotFoundException;
import com.lanchonete.lanchon.models.order.Response.OrderResponse;
import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponseDTO;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.enums.Status;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class CreateOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public CreateOrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public OrderResponseDTO create(CreateOrder order) {
        User user = userRepository.findById(order.userId())
                .orElseThrow(() -> new UserNotFoundException(order.userId()));

        Order orderCreate = new Order();
        orderCreate.setUser(user);
        orderCreate.setStatus(Status.DRAFT);
        orderCreate.setSubtotal(BigDecimal.ZERO);
        orderCreate.setTotal(BigDecimal.ZERO);
        orderCreate.setItems(new ArrayList<>());

        Order saved = orderRepository.save(orderCreate);
        return OrderResponse.toResponse(saved);
    }
}
