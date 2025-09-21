package com.lanchonete.lanchon.models.payment.entity;

import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.payment.enums.Method;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private Method method;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate created_At;
}
