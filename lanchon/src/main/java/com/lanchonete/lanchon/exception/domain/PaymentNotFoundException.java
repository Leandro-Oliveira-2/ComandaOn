package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException(Long paymentId, Long orderId) {
        super("Pagamento %d nao encontrado para o pedido %d".formatted(paymentId, orderId), HttpStatus.NOT_FOUND, "PAYMENT_NOT_FOUND");
    }
}
