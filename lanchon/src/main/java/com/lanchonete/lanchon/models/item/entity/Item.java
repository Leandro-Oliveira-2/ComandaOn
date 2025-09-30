package com.lanchonete.lanchon.models.item.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Nome do produto no momento da venda (snapshot)
    @Column(name = "name_snapshot", nullable = false, length = 250)
    private String nameSnapshot;

    // Preço unitário no momento da venda
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    @Setter(AccessLevel.NONE)
    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal = BigDecimal.ZERO;

    @Column(name = "notes", length = 500)
    private String notes = "";

    public Item(Order order, Product product, String nameSnapshot, BigDecimal unitPrice, int quantity, String notes) {
        this.order = order;
        this.product = product;
        this.nameSnapshot = (nameSnapshot != null && !nameSnapshot.isBlank())
                ? nameSnapshot
                : product != null ? product.getName() : null;
        this.notes = sanitizeNotes(notes);
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

    public void setNotes(String notes) {
        this.notes = sanitizeNotes(notes);
    }

    private void recalculateLineTotal() {
        if (unitPrice != null) {
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.lineTotal = BigDecimal.ZERO;
        }
    }

    private String sanitizeNotes(String rawNotes) {
        return rawNotes == null ? "" : rawNotes.trim();
    }

    @PrePersist
    @PreUpdate
    private void ensureConsistency() {
        if (nameSnapshot == null || nameSnapshot.isBlank()) {
            nameSnapshot = product != null ? product.getName() : "Item";
        }
        notes = sanitizeNotes(notes);
        recalculateLineTotal();
    }
}
