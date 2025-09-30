package com.lanchonete.lanchon.models.order.service;

import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.item.repository.ItemRepository;
import com.lanchonete.lanchon.models.order.Response.OrderResponse;
import com.lanchonete.lanchon.models.order.dto.OrderResponseDTO;
import com.lanchonete.lanchon.models.order.dto.UpdateOrderDTO;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Service
public class UpdateOrderService {
        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final ItemRepository itemRepository;

        public UpdateOrderService(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository) {
            this.orderRepository = orderRepository;
            this.userRepository = userRepository;
            this.itemRepository = itemRepository;
        }

    @Transactional
    public OrderResponseDTO updateOrder(Long id, UpdateOrderDTO updateOrderDTO) {
        try {
            Order orderExist = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order Não Encontrado"));

            User user = userRepository.findById(updateOrderDTO.userId())
                    .orElseThrow(() -> new RuntimeException("Usuário Não Encontrado"));

            // Atualiza status, se veio
            if (updateOrderDTO.status() != null) {
                orderExist.setStatus(updateOrderDTO.status());
            }

            // Atualiza usuário
            orderExist.setUser(user);

            // Atualiza itens
            List<Item> items = itemRepository.findAllById(updateOrderDTO.itemIds());
            var foundIds = new HashSet<>(items.stream().map(Item::getId).toList());
            var missing = updateOrderDTO.itemIds().stream().filter(missingId -> !foundIds.contains(missingId)).toList();

            if (!missing.isEmpty()) {
                throw new RuntimeException("Itens não encontrados: " + missing);
            }

            items.forEach(item -> item.setOrder(orderExist));
            orderExist.setItems(items);

            // Recalcula subtotal/total
            BigDecimal subtotal = items.stream()
                    .map(item -> item.getUnitPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            orderExist.setSubtotal(subtotal);
            orderExist.setTotal(subtotal);

            // Salva no banco
            Order saved = orderRepository.save(orderExist);

            // Retorna DTO de resposta
            return OrderResponse.toResponse(saved);

        } catch (Exception e) {
            throw e;
        }
        }


        }
