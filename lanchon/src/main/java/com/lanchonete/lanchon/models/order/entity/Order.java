package com.lanchonete.lanchon.models.order.entity;

import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.order.enums.Status;
import com.lanchonete.lanchon.models.payment.entity.Payment;
import com.lanchonete.lanchon.models.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    @OneToMany(mappedBy = "order") // "order" é o nome do campo na classe Item
    private List<Item> items;

    @Enumerated(EnumType.STRING)
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
