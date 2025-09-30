package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends DomainException {

    public CategoryNotFoundException(Long id) {
        super("Categoria com ID %d nao encontrada".formatted(id), HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND");
    }
}
