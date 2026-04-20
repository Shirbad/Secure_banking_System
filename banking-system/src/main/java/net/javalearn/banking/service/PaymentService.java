package net.javalearn.banking.service;

import net.javalearn.banking.entity.Payment;

import java.util.Optional;

public interface PaymentService {

    String pay(Long accountId, double amount);

    Optional<Payment> getPaymentStatus(String paymentId);
}