package com.lanchonete.lanchon.models.order.repository;

import com.lanchonete.lanchon.models.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository  extends JpaRepository<Order, Integer> {
    Optional<Order> findById(Long id);
}
