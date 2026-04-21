package net.javalearn.banking.controller;

import net.javalearn.banking.entity.Transaction;
import net.javalearn.banking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/fraud/flagged")
    public ResponseEntity<List<Transaction>> getFlaggedTransactions() {
        return ResponseEntity.ok(transactionService.getFlaggedTransactions());
    }

    @GetMapping("/fraud/flagged/{accountId}")
    public ResponseEntity<List<Transaction>> getFlaggedTransactionsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getFlaggedTransactionsByAccountId(accountId));
    }
}
