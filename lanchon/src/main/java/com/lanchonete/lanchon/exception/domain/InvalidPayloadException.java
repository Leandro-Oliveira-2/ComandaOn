package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidPayloadException extends DomainException {

    public InvalidPayloadException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_PAYLOAD");
    }
}
