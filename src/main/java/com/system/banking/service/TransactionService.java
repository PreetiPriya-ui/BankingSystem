package com.system.banking.service;

import com.system.banking.controller.request.TransactionRequest;
import com.system.banking.controller.response.TransactionResponse;
import com.system.banking.controller.response.TransactionStatementResponse;
import com.system.banking.model.Account;
import com.system.banking.model.Customer;
import com.system.banking.model.Transaction;
import com.system.banking.repo.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    private CustomerPrincipalService customerPrincipalService;


    @Transactional
    public void performTransaction(String userName, TransactionRequest transactionRequest) {
        Customer customer = customerPrincipalService.getCustomer(userName);
        Account fetchedAccount = accountService.findAccountByCustomer(customer.getId());
        Transaction transaction = new Transaction(transactionRequest.getTransactionType().toString(), transactionRequest.getAmount(), fetchedAccount, new Date());
        transactionRepository.save(transaction);
        fetchedAccount.setBalance((transactionRequest.getTransactionType().getMultiplicationFactor().multiply(transactionRequest.getAmount())).add(fetchedAccount.getBalance()));

    }

    public TransactionStatementResponse getStatement(String email) {
        Customer customer = customerPrincipalService.getCustomer(email);
        Account account = accountService.findAccountByCustomer(customer.getId());
        List<TransactionResponse> transactionResponse = new ArrayList<>();
        List<Transaction> transactions = transactionRepository.findByAccount_id(account.getId());

        for (Transaction transaction : transactions) {
            transactionResponse.add(getTransactionResponse(transaction));
        }

        return getTransactionStatementResponse(customer, account, transactionResponse);
    }

    private TransactionStatementResponse getTransactionStatementResponse(Customer customer, Account account, List<TransactionResponse> transactionResponse) {
        return TransactionStatementResponse
                .builder()
                .accountNumber(account.getId())
                .name(customer.getName())
                .balance(account.getBalance())
                .transactionResponse(transactionResponse)
                .build();
    }

    private TransactionResponse getTransactionResponse(Transaction transaction) {
        return TransactionResponse
                .builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .build();
    }
}
