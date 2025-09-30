package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends DomainException {

    public PasswordMismatchException() {
        super("As senhas informadas nao conferem", HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH");
    }
}
