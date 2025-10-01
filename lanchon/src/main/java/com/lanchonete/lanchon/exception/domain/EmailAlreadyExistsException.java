package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends DomainException {

    public EmailAlreadyExistsException(String email) {
        super("Email %s ja esta em uso".formatted(email), HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS");
    }
}
