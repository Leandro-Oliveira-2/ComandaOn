package com.lanchonete.lanchon.models.order.dto;

import com.lanchonete.lanchon.models.order.enums.Status;

import java.math.BigDecimal;
import java.util.List;

public record UpdateOrder(Status status,
                          BigDecimal subtotal,
                          BigDecimal total,
                          Long userId,
                          List<Long> itemIds) {
}
