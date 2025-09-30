package com.lanchonete.lanchon.models.item.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateItem(
        @JsonProperty("product_id")
        @JsonAlias("productId")
        Long productId,

        @NotBlank
        @JsonProperty("name_snapshot")
        @JsonAlias("nameSnapshot")
        String nameSnapshot,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @JsonProperty("unit_price")
        @JsonAlias("unitPrice")
        BigDecimal unitPrice,

        @NotNull
        @Min(1)
        Integer quantity,

        @JsonProperty("notes")
        @JsonAlias("notes")
        String notes
) {
}
