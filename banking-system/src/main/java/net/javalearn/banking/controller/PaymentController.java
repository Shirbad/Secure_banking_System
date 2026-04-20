package net.javalearn.banking.controller;

import net.javalearn.banking.entity.Payment;
import net.javalearn.banking.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public String pay(@RequestParam Long accountId, @RequestParam double amount) {
        return paymentService.pay(accountId, amount);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Object> getStatus(@PathVariable String paymentId) {
        Optional<Payment> paymentOptional = paymentService.getPaymentStatus(paymentId);

        if (paymentOptional.isPresent()) {
            return ResponseEntity.ok(paymentOptional.get());
        }

        return ResponseEntity.status(404).body(Map.of("message", "Payment not found"));
    }
}
