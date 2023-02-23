package com.system.banking.service;

import com.system.banking.controller.request.TransactionRequest;
import com.system.banking.controller.response.TransactionResponse;
import com.system.banking.controller.response.TransactionStatementResponse;
import com.system.banking.model.Account;
import com.system.banking.model.Customer;
import com.system.banking.model.Transaction;
import com.system.banking.model.TransactionType;
import com.system.banking.repo.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private CustomerPrincipalService customerPrincipalService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldBeAbleToPerformTransaction(){
        Customer customer = new Customer("abc", "abc@gmail.com", "1234567890", "1234", "xyz", "password");
        Account account = new Account(new BigDecimal(0), "Active", customer, new Date());
        when(customerPrincipalService.getCustomer(customer.getEmail())).thenReturn(customer);
        when(accountService.findAccountByCustomer(customer.getId())).thenReturn(account);
        BigDecimal creditAmount = new BigDecimal(5);
        Transaction transaction = new Transaction(TransactionType.CREDIT.toString(), creditAmount, account, new Date());
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.CREDIT, transaction.getAmount());

        transactionService.performTransaction(customer.getEmail(), transactionRequest);

        verify(transactionRepository).save(transaction);
    }

    @Test
    void shouldBeAbleToIncreaseInCurrentBalanceWhenThereIsAnyCredit() {
        Customer customer = new Customer("abc", "abc@gmail.com", "1234567890", "1234", "xyz", "password");
        Account account = new Account(new BigDecimal(5), "Active", customer, new Date());
        when(customerPrincipalService.getCustomer(customer.getEmail())).thenReturn(customer);
        when(accountService.findAccountByCustomer(customer.getId())).thenReturn(account);
        BigDecimal creditAmount = new BigDecimal(5);
        BigDecimal expectedBalance = new BigDecimal(10);
        Transaction transaction = new Transaction(TransactionType.CREDIT.toString(), creditAmount, account, new Date());
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.CREDIT, transaction.getAmount());

        transactionService.performTransaction(customer.getEmail(), transactionRequest);

        Assertions.assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    void shouldBeAbleToDecreaseInCurrentBalanceWhenThereIsAnyDebit(){
        Customer customer = new Customer("abc", "abc@gmail.com", "1234567890", "1234", "xyz", "password");
        Account account = new Account(new BigDecimal(5), "Active", customer, new Date());
        when(customerPrincipalService.getCustomer(customer.getEmail())).thenReturn(customer);
        when(accountService.findAccountByCustomer(customer.getId())).thenReturn(account);
        BigDecimal creditAmount = new BigDecimal(5);
        BigDecimal expectedBalance = new BigDecimal(0);
        Transaction transaction = new Transaction(TransactionType.DEBIT.toString(), creditAmount, account, new Date());
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.DEBIT, transaction.getAmount());

        transactionService.performTransaction(customer.getEmail(), transactionRequest);

        Assertions.assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    void shouldBeAbleToGetStatement() {
        List<TransactionResponse> transactions = new ArrayList<>();
        Customer customer = new Customer("abc", "abc@gmail.com", "1234567890", "1234", "xyz", "password");
        Account account = new Account(new BigDecimal(5), "Active", customer, new Date());
        Transaction transaction1 = new Transaction(TransactionType.CREDIT.toString(), new BigDecimal(20), account, new Date());
        Transaction transaction2 = new Transaction("DEBIT", new BigDecimal(5), account, new Date());
        when(customerPrincipalService.getCustomer(customer.getEmail())).thenReturn(customer);
        when(accountService.findAccountByCustomer(customer.getId())).thenReturn(account);
        TransactionRequest transactionRequestForCredit = new TransactionRequest(TransactionType.CREDIT, transaction1.getAmount());
        TransactionRequest transactionRequestForDebit = new TransactionRequest(TransactionType.CREDIT, transaction2.getAmount());
        transactionService.performTransaction(customer.getEmail(), transactionRequestForCredit);
        transactionService.performTransaction(customer.getEmail(), transactionRequestForDebit);
        TransactionResponse transactionResponse = TransactionResponse.builder().id(transaction1.getId()).transactionType(transaction1.getTransactionType()).amount(transaction1.getAmount()).build();
        TransactionResponse transactionResponse1 = TransactionResponse.builder().id(transaction2.getId()).transactionType(transaction2.getTransactionType()).amount(transaction2.getAmount()).build();
        transactions.add(transactionResponse1);
        transactions.add(transactionResponse);
        TransactionStatementResponse expectedStatement = TransactionStatementResponse.builder().accountNumber(account.getId()).name(customer.getName()).balance(account.getBalance()).transactionResponse(transactions).build();

        TransactionStatementResponse actualStatement = transactionService.getStatement(customer.getEmail());

        Assertions.assertEquals(expectedStatement, actualStatement);
    }
}
