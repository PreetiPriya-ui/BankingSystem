package com.system.banking.service;

import com.system.banking.controller.request.SignUpRequest;
import com.system.banking.exceptions.AccountNumberNotFoundException;
import com.system.banking.exceptions.EmailIdAlreadyRegisteredException;
import com.system.banking.model.Account;
import com.system.banking.model.Customer;
import com.system.banking.repo.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerPrincipalService customerPrincipalService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldBeAbleToGenerateAccountDetailsWhenCustomerDetailsAreStored() throws EmailIdAlreadyRegisteredException {
        SignUpRequest signUpRequest = new SignUpRequest("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        Customer customer = new Customer("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        Account account = new Account(new BigDecimal("0.0"), "ACTIVE", customer, new Date());

        accountService.createAccount(signUpRequest);

        verify(accountRepository).save(account);
    }

    @Test
    void shouldBeAbleToGetAccountWhenAccountNumberIsProvided() throws AccountNumberNotFoundException, EmailIdAlreadyRegisteredException {
        SignUpRequest signUpRequest = new SignUpRequest("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        Customer customer = new Customer("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        Account account = new Account(new BigDecimal("0.0"), "ACTIVE", customer, new Date());
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        accountService.createAccount(signUpRequest);

        Account fetchedAccount = accountService.getAccount(account.getId());

        Assertions.assertEquals(account, fetchedAccount);
    }

    @Test
    void shouldBeAbleToThrowExceptionWhenAccountNumberDoesNotExist() {
        long accountNumber = 8;

        assertThrows(AccountNumberNotFoundException.class, () -> accountService.getAccount(accountNumber));
    }

    @Test
    void shouldBeAbleToGetSummaryOfAccount() {
        Customer customer = new Customer("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        when(customerPrincipalService.getCustomer(customer.getEmail())).thenReturn(customer);
        Account account = new Account(new BigDecimal(0), "ACTIVE", customer, new Date());
        when(accountRepository.findByCustomerId(customer.getId())).thenReturn(account);
        Map<String, Object> expectedSummary = new HashMap<>();
        expectedSummary.put("Name", customer.getName());
        expectedSummary.put("Account Number", account.getId());
        expectedSummary.put("Balance", account.getBalance());

        Map<String, Object> actualSummary = accountService.getSummary(customer.getEmail());

        Assertions.assertEquals(expectedSummary, actualSummary);
    }
}
