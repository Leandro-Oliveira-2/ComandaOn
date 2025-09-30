package com.lanchonete.lanchon.models.item.entity;

import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @Column(name = "name_snapshot", nullable = false, length = 250)
    private String nameSnapshot;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    @Setter(AccessLevel.NONE)
    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal = BigDecimal.ZERO;

    public Item(Order order, Product product, String nameSnapshot, BigDecimal unitPrice, int quantity) {
        this.order = order;
        this.product = product;
        this.nameSnapshot = nameSnapshot;
        setUnitPrice(unitPrice);
        setQuantity(quantity);
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        recalculateLineTotal();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        recalculateLineTotal();
    }

    private void recalculateLineTotal() {
        if (unitPrice != null) {
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.lineTotal = BigDecimal.ZERO;
        }
    }
}
