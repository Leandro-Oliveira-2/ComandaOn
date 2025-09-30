package com.lanchonete.lanchon.models.category.dto;

public record CategoryResponseDTO(
        Long id,
        String name,
        Integer sortOrder,
        Boolean active
) {
}
