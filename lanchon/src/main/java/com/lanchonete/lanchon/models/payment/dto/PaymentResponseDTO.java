package com.lanchonete.lanchon.models.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.lanchon.models.payment.enums.Method;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
        Long id,
        @JsonProperty("order_id")
        Long orderId,
        Method method,
        BigDecimal amount,
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
}
