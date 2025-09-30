package com.lanchonete.lanchon.models.order.controller;

import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponseDTO;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.service.CreateOrderService;
import com.lanchonete.lanchon.models.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/order")
public class OrderController {
    private final CreateOrderService createOrderService;
    private final OrderService orderService;
    
    public OrderController(CreateOrderService createOrderServic1e,  OrderService orderService1) {
        this.createOrderService = createOrderServic1e;
        this.orderService = orderService1;
    }


    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid CreateOrder order) {
        OrderResponseDTO response = createOrderService.create(order);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public List<Order> findAll() {
        return orderService.findAll();
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<OrderResponse> update(@PathVariable Long id, @RequestBody UpdateOrder dto) {
//        OrderResponse response = orderService.updateOrder(id, dto);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<OrderResponse>> getOrders() {
//        return ResponseEntity.ok(orderService.getOrders());
//    }
}