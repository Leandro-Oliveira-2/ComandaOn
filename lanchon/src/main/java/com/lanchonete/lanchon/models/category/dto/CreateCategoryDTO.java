package com.lanchonete.lanchon.models.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.lanchon.models.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryDTO(
        @NotBlank String name,
        @NotNull @JsonProperty("sort_order") Integer sortOrder,
        @NotNull Boolean active
) {
    public CreateCategoryDTO(Category category) {
        this(category.getName(), category.getSort_order(), category.getActive());
    }
}