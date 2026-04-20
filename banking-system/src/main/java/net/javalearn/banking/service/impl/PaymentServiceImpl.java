package net.javalearn.banking.service.impl;

import net.javalearn.banking.entity.Account;
import net.javalearn.banking.entity.Payment;
import net.javalearn.banking.enums.PaymentStatus;
import net.javalearn.banking.exception.ResourceNotFoundException;
import net.javalearn.banking.repository.AccountRepository;
import net.javalearn.banking.repository.PaymentRepository;
import net.javalearn.banking.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private AccountRepository accountRepository;
    private PaymentRepository paymentRepository;

    private static int counter = 1;

    public PaymentServiceImpl(AccountRepository accountRepository,
                              PaymentRepository paymentRepository) {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public String pay(Long accountId, double amount) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance");
        }

        // Deduct money
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        // Cashback 2%
        double cashback = Math.floor(amount * 0.02);

        Payment payment = new Payment();
        payment.setPaymentId("payment" + System.currentTimeMillis());
        payment.setAccountId(accountId);
        payment.setAmount(amount);
        payment.setCashbackAmount(cashback);
        payment.setCashbackTime(LocalDateTime.now().plusSeconds(10));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setStatus(PaymentStatus.COMPLETED);

        paymentRepository.save(payment);

        return payment.getPaymentId();
    }

    @Override
    public Optional<Payment> getPaymentStatus(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId);
    }
}
