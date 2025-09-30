package com.lanchonete.lanchon.models.order.controller;

import com.lanchonete.lanchon.models.order.Response.OrderResponse;
import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponseDTO;
import com.lanchonete.lanchon.models.order.service.CreateOrderService;
import com.lanchonete.lanchon.models.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final CreateOrderService createOrderService;
    private final OrderService orderService;

    public OrderController(CreateOrderService createOrderService, OrderService orderService) {
        this.createOrderService = createOrderService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid CreateOrder order) {
        OrderResponseDTO response = createOrderService.create(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<OrderResponseDTO> findAll() {
        return orderService.findAll().stream()
                .map(OrderResponse::toResponse)
                .toList();
    }
}
