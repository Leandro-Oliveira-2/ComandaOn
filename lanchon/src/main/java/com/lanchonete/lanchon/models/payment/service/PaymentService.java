package com.lanchonete.lanchon.models.payment.service;

import com.lanchonete.lanchon.models.order.entity.Order;
import com.lanchonete.lanchon.models.order.enums.Status;
import com.lanchonete.lanchon.models.order.repository.OrderRepository;
import com.lanchonete.lanchon.models.payment.dto.CreatePaymentDTO;
import com.lanchonete.lanchon.models.payment.dto.PaymentResponseDTO;
import com.lanchonete.lanchon.models.payment.dto.UpdatePaymentDTO;
import com.lanchonete.lanchon.models.payment.entity.Payment;
import com.lanchonete.lanchon.models.payment.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentResponseDTO create(Long orderId, CreatePaymentDTO dto) {
        Order order = getOrder(orderId);
        ensureOrderAcceptsPayment(order);

        validateAmount(dto.amount());

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(dto.method());
        payment.setAmount(dto.amount());

        Payment saved = paymentRepository.save(payment);

        if (order.getPayments() == null) {
            order.setPayments(new ArrayList<>());
        }
        order.getPayments().add(saved);

        refreshOrderPaymentStatus(order);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> listByOrder(Long orderId) {
        ensureOrderExists(orderId);
        return paymentRepository.findByOrder_Id(orderId).stream()
                .map(PaymentService::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO getById(Long orderId, Long paymentId) {
        ensureOrderExists(orderId);
        Payment payment = findPayment(orderId, paymentId);
        return toDto(payment);
    }

    @Transactional
    public PaymentResponseDTO update(Long orderId, Long paymentId, UpdatePaymentDTO dto) {
        Order order = getOrder(orderId);
        ensureOrderAcceptsPayment(order);

        Payment payment = findPayment(orderId, paymentId);

        if (dto.method() != null) {
            payment.setMethod(dto.method());
        }
        if (dto.amount() != null) {
            validateAmount(dto.amount());
            payment.setAmount(dto.amount());
        }

        Payment saved = paymentRepository.save(payment);
        refreshOrderPaymentStatus(order);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long orderId, Long paymentId) {
        Order order = getOrder(orderId);
        ensureOrderAcceptsPayment(order);

        Payment payment = findPayment(orderId, paymentId);
        paymentRepository.delete(payment);

        if (order.getPayments() != null) {
            order.getPayments().removeIf(existing -> existing.getId().equals(paymentId));
        }

        refreshOrderPaymentStatus(order);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    private void ensureOrderExists(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found: " + orderId);
        }
    }

    private Payment findPayment(Long orderId, Long paymentId) {
        return paymentRepository.findById(paymentId)
                .filter(payment -> payment.getOrder() != null && payment.getOrder().getId().equals(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Payment not found for order: " + orderId));
    }

    private void ensureOrderAcceptsPayment(Order order) {
        if (order.getStatus() == Status.CANCELED) {
            throw new IllegalStateException("Cannot register payments for canceled orders");
        }
    }

    private void refreshOrderPaymentStatus(Order order) {
        BigDecimal orderTotal = order.getTotal() != null ? order.getTotal() : BigDecimal.ZERO;
        BigDecimal paidTotal = paymentRepository.sumAmountByOrder(order.getId());

        if (orderTotal.compareTo(BigDecimal.ZERO) > 0 && paidTotal.compareTo(orderTotal) >= 0) {
            if (order.getStatus() != Status.PAID) {
                order.setStatus(Status.PAID);
                orderRepository.save(order);
            }
        } else if (order.getStatus() == Status.PAID && paidTotal.compareTo(orderTotal) < 0) {
            order.setStatus(Status.DRAFT);
            orderRepository.save(order);
        }
    }

    private static void validateAmount(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
    }

    private static PaymentResponseDTO toDto(Payment payment) {
        Long orderId = payment.getOrder() != null ? payment.getOrder().getId() : null;
        return new PaymentResponseDTO(
                payment.getId(),
                orderId,
                payment.getMethod(),
                payment.getAmount(),
                payment.getCreatedAt()
        );
    }
}
