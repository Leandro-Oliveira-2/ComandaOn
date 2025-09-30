package com.lanchonete.lanchon.models.order.service;

import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}