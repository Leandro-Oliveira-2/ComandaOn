package com.lanchonete.lanchon.models.order.controller;

import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponse;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.service.OrderService;
import com.lanchonete.lanchon.models.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrder dto) {
        OrderResponse response = orderService.createOrder(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public List<OrderResponse> getUsers() {
        return orderService.getOrders();
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
    }
}
