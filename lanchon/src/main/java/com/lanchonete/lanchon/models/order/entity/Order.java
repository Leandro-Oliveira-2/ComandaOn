package com.lanchonete.lanchon.models.order.entity;

import com.lanchonete.lanchon.models.order.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Timer;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int created_by;

    @NotBlank
    private Status status;

    @NotNull
    private double  subtotal;

    @NotNull
    private double  total;

    @NotNull
    private LocalDateTime created_at;

    @NotNull
    private LocalDateTime updated_at;
}
