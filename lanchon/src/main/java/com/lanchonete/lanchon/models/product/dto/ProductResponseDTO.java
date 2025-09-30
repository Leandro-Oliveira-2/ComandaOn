package com.lanchonete.lanchon.models.product.dto;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        Boolean active,
        Long categoryId,
        String categoryName
) {
}
