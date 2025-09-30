package com.lanchonete.lanchon.models.payment.repository;

import com.lanchonete.lanchon.models.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrder_Id(Long orderId);

    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.order.id = :orderId")
    BigDecimal sumAmountByOrder(@Param("orderId") Long orderId);
}
