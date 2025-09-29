package com.lanchonete.lanchon.models.product.dto;

public record UpdateProductDTO(
        int id,
        Integer categoryId,
        String categoryName,
        String name,
        String description,
        double price,
        Boolean active
    ) { }
