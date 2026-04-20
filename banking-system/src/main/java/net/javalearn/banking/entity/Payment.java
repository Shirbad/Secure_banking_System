package net.javalearn.banking.entity;

import jakarta.persistence.*;
import lombok.*;
import net.javalearn.banking.enums.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentId; // payment1, payment2

    private Long accountId;

    private double amount;

    private double cashbackAmount;

    private LocalDateTime cashbackTime;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, COMPLETED
}