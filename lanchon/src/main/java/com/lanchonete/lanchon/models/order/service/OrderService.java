package com.lanchonete.lanchon.models.order.service;

import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order){
        return orderRepository.save(order);
    }


}
