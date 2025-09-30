package com.lanchonete.lanchon.models.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProductDTO(
        @NotNull @JsonProperty("category_id") Long categoryId,
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) Double price,
        @NotNull Boolean active
) {
}
