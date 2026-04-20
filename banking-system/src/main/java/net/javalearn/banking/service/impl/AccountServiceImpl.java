package net.javalearn.banking.service.impl;

import net.javalearn.banking.dto.AccountDto;
import net.javalearn.banking.entity.Account;
import net.javalearn.banking.entity.Payment;
import net.javalearn.banking.enums.PaymentStatus;
import net.javalearn.banking.exception.ResourceNotFoundException;
import net.javalearn.banking.mapper.AccountMapper;
import net.javalearn.banking.repository.AccountRepository;
import net.javalearn.banking.repository.PaymentRepository;
import net.javalearn.banking.service.AccountService;
import net.javalearn.banking.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final PaymentRepository paymentRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              TransactionService transactionService,
                              PaymentRepository paymentRepository) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountbyId(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setBalance(account.getBalance() + amount);

        Account savedAccount = accountRepository.save(account);
        transactionService.saveTransaction("DEPOSIT", amount, id, null);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Account savedAccount = accountRepository.save(account);
        transactionService.saveTransaction("WITHDRAW", amount, id, null);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto transfer(Long sourceId, Long targetId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        Account sourceAccount = accountRepository.findById(sourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account targetAccount = accountRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("Target account not found"));

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance");
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
        transactionService.saveTransaction("TRANSFER", amount, sourceId, targetId);

        return AccountMapper.mapToAccountDto(sourceAccount);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::mapToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopSpenders(int n) {
        if (n <= 0) {
            return List.of();
        }

        List<Account> accounts = accountRepository.findAll();
        List<Payment> payments = paymentRepository.findByStatus(PaymentStatus.COMPLETED);

        Map<Long, Double> spendingMap = new HashMap<>();
        for (Account account : accounts) {
            spendingMap.put(account.getId(), 0.0);
        }

        for (Payment payment : payments) {
            Long accountId = payment.getAccountId();
            spendingMap.put(accountId, spendingMap.getOrDefault(accountId, 0.0) + payment.getAmount());
        }

        List<Map.Entry<Long, Double>> sortedList = new ArrayList<>(spendingMap.entrySet());
        sortedList.sort((left, right) -> {
            int amountCompare = Double.compare(right.getValue(), left.getValue());
            if (amountCompare == 0) {
                return left.getKey().compareTo(right.getKey());
            }
            return amountCompare;
        });

        List<String> result = new ArrayList<>();
        for (int index = 0; index < Math.min(n, sortedList.size()); index++) {
            Long accountId = sortedList.get(index).getKey();
            Double totalSpent = sortedList.get(index).getValue();
            result.add("account" + accountId + "(" + String.format(Locale.US, "%.2f", totalSpent) + ")");
        }
        return result;
    }
}
