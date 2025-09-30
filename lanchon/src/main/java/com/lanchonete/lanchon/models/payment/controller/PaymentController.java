package com.lanchonete.lanchon.models.payment.controller;

import com.lanchonete.lanchon.models.payment.dto.CreatePaymentDTO;
import com.lanchonete.lanchon.models.payment.dto.PaymentResponseDTO;
import com.lanchonete.lanchon.models.payment.dto.UpdatePaymentDTO;
import com.lanchonete.lanchon.models.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/payments")
@Tag(name = "Pagamentos", description = "Controle de recebimentos e conciliacao de pedidos.")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<PaymentResponseDTO> listPayments(@PathVariable Long orderId) {
        return paymentService.listByOrder(orderId);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Pagamento nao encontrado para o pedido")
    })
    @GetMapping("/{paymentId}")
    public PaymentResponseDTO getPayment(@PathVariable Long orderId, @PathVariable Long paymentId) {
        return paymentService.getById(orderId, paymentId);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pagamento registrado"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos"),
            @ApiResponse(responseCode = "404", description = "Pedido nao encontrado")
    })
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@PathVariable Long orderId, @Valid @RequestBody CreatePaymentDTO dto) {
        PaymentResponseDTO created = paymentService.create(orderId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento atualizado"),
            @ApiResponse(responseCode = "404", description = "Pagamento nao encontrado para o pedido")
    })
    @PatchMapping("/{paymentId}")
    public PaymentResponseDTO updatePayment(@PathVariable Long orderId,
                                            @PathVariable Long paymentId,
                                            @RequestBody UpdatePaymentDTO dto) {
        return paymentService.update(orderId, paymentId, dto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pagamento removido"),
            @ApiResponse(responseCode = "404", description = "Pagamento nao encontrado para o pedido")
    })
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long orderId, @PathVariable Long paymentId) {
        paymentService.delete(orderId, paymentId);
        return ResponseEntity.noContent().build();
    }
}
