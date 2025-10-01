package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(Long id) {
        super("Pedido com ID %d nao encontrado".formatted(id), HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND");
    }
}
