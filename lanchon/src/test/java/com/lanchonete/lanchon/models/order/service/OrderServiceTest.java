package com.lanchonete.lanchon.models.order.service;

import com.lanchonete.lanchon.models.item.entity.Item;
import com.lanchonete.lanchon.models.item.repository.ItemRepository;
import com.lanchonete.lanchon.models.order.dto.CreateOrder;
import com.lanchonete.lanchon.models.order.dto.OrderResponse;
import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.enums.Status;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.user.entity.User;
import com.lanchonete.lanchon.models.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_successfullyPersistsOrder() {
        Long userId = 7L;
        List<Long> itemIds = List.of(11L, 12L);
        CreateOrder request = new CreateOrder(
                Status.RECEIVED,
                new BigDecimal("17.50"),
                new BigDecimal("20.00"),
                userId,
                itemIds
        );

        User user = new User();
        user.setId(userId);

        Item itemOne = new Item();
        itemOne.setId(11L);
        Item itemTwo = new Item();
        itemTwo.setId(12L);
        List<Item> fetchedItems = List.of(itemOne, itemTwo);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAllById(itemIds)).thenReturn(fetchedItems);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order toPersist = invocation.getArgument(0, Order.class);
            toPersist.setId(99L);
            LocalDateTime timestamp = LocalDateTime.parse("2024-01-01T10:15:30");
            toPersist.setCreatedAt(timestamp);
            toPersist.setUpdatedAt(timestamp);
            return toPersist;
        });

        OrderResponse response = orderService.createOrder(request);

        assertEquals(99L, response.id());
        assertEquals(Status.RECEIVED, response.status());
        assertEquals(new BigDecimal("17.50"), response.subtotal());
        assertEquals(new BigDecimal("20.00"), response.total());
        assertEquals(userId, response.userId());
        assertEquals(itemIds, response.itemIds());
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order persisted = orderCaptor.getValue();
        assertEquals(user, persisted.getUser());
        assertEquals(Status.RECEIVED, persisted.getStatus());
        assertEquals(new BigDecimal("17.50"), persisted.getSubtotal());
        assertEquals(new BigDecimal("20.00"), persisted.getTotal());
        assertEquals(fetchedItems, persisted.getItems());
        assertTrue(fetchedItems.stream().allMatch(item -> item.getOrder() == persisted));
    }

    @Test
    void createOrder_whenUserDoesNotExist_throwsRuntimeException() {
        Long missingUserId = 42L;
        CreateOrder request = new CreateOrder(
                Status.RECEIVED,
                new BigDecimal("10.00"),
                new BigDecimal("12.00"),
                missingUserId,
                List.of(1L)
        );

        when(userRepository.findById(missingUserId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_whenItemListIsEmpty_throwsRuntimeException() {
        Long userId = 5L;
        CreateOrder request = new CreateOrder(
                Status.RECEIVED,
                new BigDecimal("9.99"),
                new BigDecimal("9.99"),
                userId,
                List.of()
        );

        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_whenItemsAreMissing_throwsRuntimeException() {
        Long userId = 2L;
        List<Long> itemIds = List.of(10L, 20L);
        CreateOrder request = new CreateOrder(
                Status.RECEIVED,
                new BigDecimal("5.00"),
                new BigDecimal("7.00"),
                userId,
                itemIds
        );

        User user = new User();
        user.setId(userId);

        Item existingItem = new Item();
        existingItem.setId(10L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findAllById(itemIds)).thenReturn(List.of(existingItem));

        assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrder_successfullyUpdatesOrder() {
        Long orderId = 200L;
        Long originalUserId = 1L;
        Long newUserId = 2L;

        User originalUser = new User();
        originalUser.setId(originalUserId);

        Item existingItem = new Item();
        existingItem.setId(30L);

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(Status.DRAFT);
        existingOrder.setSubtotal(new BigDecimal("10.00"));
        existingOrder.setTotal(new BigDecimal("11.00"));
        existingOrder.setUser(originalUser);
        existingOrder.setItems(List.of(existingItem));

        UpdateOrder update = new UpdateOrder(
                Status.PAID,
                new BigDecimal("15.00"),
                new BigDecimal("18.00"),
                newUserId,
                List.of(40L, 50L)
        );

        User newUser = new User();
        newUser.setId(newUserId);

        Item itemOne = new Item();
        itemOne.setId(40L);
        Item itemTwo = new Item();
        itemTwo.setId(50L);
        List<Item> fetchedItems = List.of(itemOne, itemTwo);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(userRepository.findById(newUserId)).thenReturn(Optional.of(newUser));
        when(itemRepository.findAllById(update.itemIds())).thenReturn(fetchedItems);
        when(orderRepository.save(existingOrder)).thenAnswer(invocation -> {
            existingOrder.setUpdatedAt(LocalDateTime.parse("2024-02-01T12:00:00"));
            return existingOrder;
        });

        OrderResponse response = orderService.updateOrder(orderId, update);

        assertEquals(orderId, response.id());
        assertEquals(Status.PAID, response.status());
        assertEquals(new BigDecimal("15.00"), response.subtotal());
        assertEquals(new BigDecimal("18.00"), response.total());
        assertEquals(newUserId, response.userId());
        assertEquals(update.itemIds(), response.itemIds());

        assertEquals(newUser, existingOrder.getUser());
        assertEquals(fetchedItems, existingOrder.getItems());
        assertTrue(fetchedItems.stream().allMatch(item -> item.getOrder() == existingOrder));
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void updateOrder_whenOrderDoesNotExist_throwsRuntimeException() {
        Long orderId = 999L;
        UpdateOrder update = new UpdateOrder(null, null, null, null, null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.updateOrder(orderId, update));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrder_whenNewUserDoesNotExist_throwsRuntimeException() {
        Long orderId = 10L;
        Long newUserId = 20L;

        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        UpdateOrder update = new UpdateOrder(Status.READY, null, null, newUserId, null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(userRepository.findById(newUserId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.updateOrder(orderId, update));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrder_whenNewItemsMissing_throwsRuntimeException() {
        Long orderId = 55L;
        List<Long> requestedItems = List.of(1L, 2L);

        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        UpdateOrder update = new UpdateOrder(Status.READY, null, null, null, requestedItems);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        Item fetchedItem = new Item();
fetchedItem.setId(1L);
when(itemRepository.findAllById(requestedItems)).thenReturn(List.of(fetchedItem));

        assertThrows(RuntimeException.class, () -> orderService.updateOrder(orderId, update));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrder_whenNewItemsListIsEmpty_throwsRuntimeException() {
        Long orderId = 70L;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        UpdateOrder update = new UpdateOrder(Status.READY, null, null, null, List.of());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        assertThrows(RuntimeException.class, () -> orderService.updateOrder(orderId, update));
        verify(orderRepository, never()).save(any());
    }
}