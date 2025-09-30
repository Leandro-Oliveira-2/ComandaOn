package com.lanchonete.lanchon.models.item.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record UpdateItem(
        @JsonProperty("name_snapshot")
        @JsonAlias("nameSnapshot")
        String nameSnapshot,

        @JsonProperty("unit_price")
        @JsonAlias("unitPrice")
        BigDecimal unitPrice,

        Integer quantity,

        @JsonProperty("notes")
        @JsonAlias("notes")
        String notes
) {
}
