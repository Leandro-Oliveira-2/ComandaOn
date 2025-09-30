package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainException {

    public UserNotFoundException(Long id) {
        super("Usuario com ID %d nao encontrado".formatted(id), HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
}
