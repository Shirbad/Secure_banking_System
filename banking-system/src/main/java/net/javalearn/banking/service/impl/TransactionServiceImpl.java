package net.javalearn.banking.service.impl;

import net.javalearn.banking.entity.Transaction;
import net.javalearn.banking.repository.TransactionRepository;
import net.javalearn.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Value("${app.fraud.large-transfer-threshold}")
    private double largeTransferThreshold;

    @Value("${app.fraud.max-transactions-per-minute}")
    private long maxTransactionsPerMinute;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(String type, double amount, Long sourceId, Long targetId) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setSourceAccountId(sourceId);
        transaction.setTargetAccountId(targetId);
        transaction.setTimestamp(LocalDateTime.now());

        List<String> fraudReasons = evaluateFraudRules(type, amount, sourceId, targetId, transaction.getTimestamp());
        transaction.setFraudFlagged(!fraudReasons.isEmpty());
        transaction.setFraudReason(String.join("; ", fraudReasons));

        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findBySourceAccountIdOrTargetAccountId(accountId, accountId);
    }

    @Override
    public List<Transaction> getFlaggedTransactions() {
        return transactionRepository.findByFraudFlaggedTrue();
    }

    @Override
    public List<Transaction> getFlaggedTransactionsByAccountId(Long accountId) {
        return transactionRepository
                .findByFraudFlaggedTrueAndSourceAccountIdOrFraudFlaggedTrueAndTargetAccountId(accountId, accountId);
    }

    private List<String> evaluateFraudRules(String type,
                                            double amount,
                                            Long sourceId,
                                            Long targetId,
                                            LocalDateTime currentTimestamp) {
        List<String> fraudReasons = new ArrayList<>();

        if ("TRANSFER".equalsIgnoreCase(type) && amount >= largeTransferThreshold) {
            fraudReasons.add("Large transfer detected");
        }

        Long monitoredAccountId = sourceId != null ? sourceId : targetId;
        if (monitoredAccountId != null) {
            LocalDateTime oneMinuteAgo = currentTimestamp.minusMinutes(1);
            long recentTransactionCount = transactionRepository
                    .countByTimestampAfterAndSourceAccountIdOrTimestampAfterAndTargetAccountId(
                            oneMinuteAgo,
                            monitoredAccountId,
                            oneMinuteAgo,
                            monitoredAccountId
                    );

            if (recentTransactionCount >= maxTransactionsPerMinute) {
                fraudReasons.add("Multiple transactions detected within one minute");
            }
        }

        return fraudReasons;
    }
}
