package com.system.banking.service;

import com.system.banking.controller.request.SignUpRequest;
import com.system.banking.exceptions.AccountNumberNotFoundException;
import com.system.banking.exceptions.EmailIdAlreadyRegisteredException;
import com.system.banking.model.Account;
import com.system.banking.model.Customer;
import com.system.banking.repo.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;

    private CustomerPrincipalService customerPrincipalService;

    public void createAccount(SignUpRequest signUpRequest) throws EmailIdAlreadyRegisteredException {
        // check customer exist
        customerPrincipalService.saveCustomer(signUpRequest);
        Customer savedCustomer = customerPrincipalService.getCustomer(signUpRequest.getEmail());
        Account account = new Account(new BigDecimal(0), "ACTIVE", savedCustomer, new Date());
        accountRepository.save(account);

    }

    public Account getAccount(Long accountNumber) throws AccountNumberNotFoundException {
        return accountRepository.findById(accountNumber).orElseThrow(AccountNumberNotFoundException::new);

    }

    public Account findAccountByCustomer(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Map<String, Object> getSummary(String email) {
        Map<String, Object> summary = new HashMap<>();
        Customer customer = customerPrincipalService.getCustomer(email);
        String name = customer.getName();
        Account account = accountRepository.findByCustomerId(customer.getId());
        summary.put("Name", name);
        summary.put("Account Number", account.getId());
        summary.put("Balance", account.getBalance());
        return summary;

    }

}
