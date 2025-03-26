package auction.controllers;

import auction.entities.Payment;
import auction.entities.DTO.PaymentDTO;
import auction.entities.enums.PaymentStatus;
import auction.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create/{bidId}")
    public ResponseEntity<PaymentDTO> createPayment(@PathVariable Long bidId) {
        PaymentDTO payment = paymentService.createPayment(bidId);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable Long paymentId, @RequestParam PaymentStatus status) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(customerId));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(paymentService.getPaymentsBySeller(sellerId));
    }
}
