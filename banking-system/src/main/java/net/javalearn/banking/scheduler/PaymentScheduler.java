package net.javalearn.banking.scheduler;

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
public class PaymentScheduler {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    public PaymentScheduler(PaymentRepository paymentRepository,
                            AccountRepository accountRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
    }

    // 🔥 Runs every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void processCashback() {

        List<Payment> payments = paymentRepository.findByStatus(PaymentStatus.PENDING);

        for (Payment payment : payments) {

            // check if cashback time reached
            if (payment.getCashbackTime().isBefore(LocalDateTime.now())) {

                Account account = accountRepository.findById(payment.getAccountId())
                        .orElseThrow(() -> new RuntimeException("Account not found"));

                // add cashback
                account.setBalance(account.getBalance() + payment.getCashbackAmount());
                accountRepository.save(account);

                // update payment status
                payment.setStatus(PaymentStatus.COMPLETED);
                paymentRepository.save(payment);

                System.out.println("Cashback processed for: " + payment.getPaymentId());
            }
        }
    }
}