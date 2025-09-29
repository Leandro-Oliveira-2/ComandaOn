package com.lanchonete.lanchon.models.order.dto;

import com.lanchonete.lanchon.models.order.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateOrder(
        @NotNull Status status,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal subtotal,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal total,
        @NotNull Long userId,
        @NotNull List<Long> itemIds
) { }


