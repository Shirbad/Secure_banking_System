package net.javalearn.banking.service;

import net.javalearn.banking.dto.AccountDto;
import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountbyId(Long id);

    AccountDto deposit(Long id, double amount);

    AccountDto withdraw(Long id, double amount);

    AccountDto transfer(Long sourceId, Long targetId, double amount);

    List<AccountDto> getAllAccounts();

    List<String> getTopSpenders(int n); // ✅ Added
}