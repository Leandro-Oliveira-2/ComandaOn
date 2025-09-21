package com.lanchonete.lanchon.models.item.entity;

import com.lanchonete.lanchon.models.category.entity.Category;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotBlank
    private String name;

    @NotNull
    private double price;

    @NotNull
    private int quantity;

    @NotNull
    private String notes;
}
