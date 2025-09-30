package com.lanchonete.lanchon.models.order.Response;

import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.order.dto.OrderResponseDTO;
import com.lanchonete.lanchon.models.order.entity.Order;

import java.util.List;

public class OrderResponse {

    public static OrderResponseDTO toResponse(Order order) {
        List<Long> itemIds = order.getItems() == null ? List.of() :
                order.getItems().stream().map(Item::getId).toList();

        Long userId = order.getUser() != null ? order.getUser().getId() : null;

        return new OrderResponseDTO(
                order.getId(),
                order.getStatus(),
                order.getSubtotal(),
                order.getTotal(),
                userId,
                itemIds,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
