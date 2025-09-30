package com.lanchonete.lanchon.models.order.controller;

import com.lanchonete.lanchon.models.order.Response.OrderResponse;
import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponseDTO;
import com.lanchonete.lanchon.models.order.service.CreateOrderService;
import com.lanchonete.lanchon.models.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido criado"),
            @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    })
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
