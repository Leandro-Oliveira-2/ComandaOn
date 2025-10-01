package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidOrderStatusException extends DomainException {

    public InvalidOrderStatusException(String message) {
        super(message, HttpStatus.CONFLICT, "INVALID_ORDER_STATUS");
    }
}
