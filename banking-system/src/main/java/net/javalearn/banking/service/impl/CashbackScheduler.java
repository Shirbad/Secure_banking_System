package net.javalearn.banking.service.impl;

import net.javalearn.banking.entity.Account;
import net.javalearn.banking.entity.Payment;
import net.javalearn.banking.enums.PaymentStatus;
import net.javalearn.banking.repository.AccountRepository;
import net.javalearn.banking.repository.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CashbackScheduler {

    private PaymentRepository paymentRepository;
    private AccountRepository accountRepository;

    public CashbackScheduler(PaymentRepository paymentRepository,
                             AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void processCashback() {

        List<Payment> payments = paymentRepository.findAll();

        for (Payment payment : payments) {

            if (payment.getStatus() == PaymentStatus.PENDING &&
                    payment.getCashbackTime().isBefore(LocalDateTime.now())) {

                Account account = accountRepository.findById(payment.getAccountId())
                        .orElseThrow(() -> new RuntimeException("Account not found"));

                // Add cashback
                account.setBalance(account.getBalance() + payment.getCashbackAmount());
                accountRepository.save(account);

                // Update status
                payment.setStatus(PaymentStatus.COMPLETED);
                paymentRepository.save(payment);
            }
        }
    }
}