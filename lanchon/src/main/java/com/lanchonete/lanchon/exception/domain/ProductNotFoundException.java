package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends DomainException {

    public ProductNotFoundException(Long id) {
        super("Produto com ID %d nao encontrado".formatted(id), HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND");
    }
}
