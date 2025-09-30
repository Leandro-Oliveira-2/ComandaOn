package com.lanchonete.lanchon.models.item.dto;

import java.math.BigDecimal;

public record ItemDTO(Long id,
                      Long productId,
                      String nameSnapshot,
                      BigDecimal unitPrice,
                      Integer quantity,
                      BigDecimal lineTotal) {
}
