package net.javalearn.banking.repository;

import net.javalearn.banking.entity.Payment;
import net.javalearn.banking.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByStatus(PaymentStatus status);
}