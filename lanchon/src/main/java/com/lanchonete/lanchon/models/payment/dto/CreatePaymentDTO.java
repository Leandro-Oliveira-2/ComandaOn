package com.lanchonete.lanchon.models.payment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.lanchon.models.payment.enums.Method;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentDTO(
        @NotNull
        Method method,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @JsonProperty("amount")
        @JsonAlias("valor")
        BigDecimal amount
) {
}
