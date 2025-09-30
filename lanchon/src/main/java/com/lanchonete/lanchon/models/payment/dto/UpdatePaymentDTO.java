package com.lanchonete.lanchon.models.payment.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.lanchon.models.payment.enums.Method;

import java.math.BigDecimal;

public record UpdatePaymentDTO(
        Method method,

        @JsonProperty("amount")
        @JsonAlias("valor")
        BigDecimal amount
) {
}
