package net.javalearn.banking.service;

import net.javalearn.banking.entity.Transaction;

import java.util.List;

public interface TransactionService {

    void saveTransaction(String type, double amount, Long sourceId, Long targetId);

    List<Transaction> getTransactionsByAccountId(Long accountId);
}