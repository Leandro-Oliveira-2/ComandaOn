package com.lanchonete.lanchon.models.order.dto;

import com.lanchonete.lanchon.models.order.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(Long id,
                               Status status,
                               BigDecimal subtotal,
                               BigDecimal total,
                               Long userId,
                               List<Long> itemIds,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt
) { }
