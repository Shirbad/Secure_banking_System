package net.javalearn.banking.controller;

import net.javalearn.banking.dto.AccountDto;
import net.javalearn.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountByID(@PathVariable Long id) {
        AccountDto accountDto = accountService.getAccountbyId(id);
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        AccountDto accountDto = accountService.deposit(id, request.get("amount"));
        return ResponseEntity.ok(accountDto);
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable long id, @RequestBody Map<String, Double> request) {
        double amount = request.get("amount");
        AccountDto accountDto = accountService.withdraw(id, amount);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/transfer")
    public ResponseEntity<AccountDto> transfer(@RequestBody Map<String, Object> request) {
        Long sourceId = Long.valueOf(request.get("sourceId").toString());
        Long targetId = Long.valueOf(request.get("targetId").toString());
        Double amount = Double.valueOf(request.get("amount").toString());

        AccountDto accountDto = accountService.transfer(sourceId, targetId, amount);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/top-spenders")
    public ResponseEntity<List<String>> getTopSpenders(@RequestParam int n) {
        return ResponseEntity.ok(accountService.getTopSpenders(n));
    }
}
