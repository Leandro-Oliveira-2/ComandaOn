package com.lanchonete.lanchon.exception.domain;

import com.lanchonete.lanchon.exception.DomainException;
import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends DomainException {

    public ItemNotFoundException(Long itemId, Long orderId) {
        super("Item %d nao encontrado no pedido %d".formatted(itemId, orderId), HttpStatus.NOT_FOUND, "ITEM_NOT_FOUND");
    }
}
