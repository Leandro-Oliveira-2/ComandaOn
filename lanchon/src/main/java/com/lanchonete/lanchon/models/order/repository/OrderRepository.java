package com.lanchonete.lanchon.models.order.repository;

import com.lanchonete.lanchon.models.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order, Integer> {
}
