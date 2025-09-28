package com.lanchonete.lanchon.models.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lanchonete.lanchon.models.category.entity.Category;

public record UpdateCategoryDTO(
        String name,
        @JsonProperty("sort_order") Integer sortOrder,
        Boolean active
) {
    public UpdateCategoryDTO(Category category) {
        this(category.getName(), category.getSort_order(), category.getActive());
    }
}