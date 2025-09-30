package com.lanchonete.lanchon.models.order.dto;
import jakarta.validation.constraints.NotNull;

public record CreateOrder(
        @NotNull Long userId
) { }


