package com.lanchonete.lanchon.models.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record UpdateItem(
        @JsonProperty("name_snapshot") String nameSnapshot,
        @JsonProperty("unit_price") BigDecimal unitPrice,
        Integer quantity
) {
}