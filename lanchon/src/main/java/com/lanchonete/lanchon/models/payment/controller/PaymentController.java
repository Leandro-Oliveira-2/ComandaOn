package com.lanchonete.lanchon.models.payment.controller;

import com.lanchonete.lanchon.models.payment.dto.CreatePaymentDTO;
import com.lanchonete.lanchon.models.payment.dto.PaymentResponseDTO;
import com.lanchonete.lanchon.models.payment.dto.UpdatePaymentDTO;
import com.lanchonete.lanchon.models.payment.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentResponseDTO> listPayments(@PathVariable Long orderId) {
        return paymentService.listByOrder(orderId);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponseDTO getPayment(@PathVariable Long orderId, @PathVariable Long paymentId) {
        return paymentService.getById(orderId, paymentId);
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@PathVariable Long orderId, @Valid @RequestBody CreatePaymentDTO dto) {
        PaymentResponseDTO created = paymentService.create(orderId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{paymentId}")
    public PaymentResponseDTO updatePayment(@PathVariable Long orderId,
                                            @PathVariable Long paymentId,
                                            @RequestBody UpdatePaymentDTO dto) {
        return paymentService.update(orderId, paymentId, dto);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long orderId, @PathVariable Long paymentId) {
        paymentService.delete(orderId, paymentId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
