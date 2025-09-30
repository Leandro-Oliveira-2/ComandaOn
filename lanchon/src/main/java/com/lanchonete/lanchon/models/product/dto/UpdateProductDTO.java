package com.lanchonete.lanchon.models.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProductDTO(
        @JsonProperty("category_id") Long categoryId,
        String name,
        String description,
        Double price,
        Boolean active
) { }
