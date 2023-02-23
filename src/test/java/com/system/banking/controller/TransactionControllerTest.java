package com.system.banking.controller;

import com.system.banking.controller.request.TransactionRequest;
import com.system.banking.exceptions.AccountNumberNotFoundException;
import com.system.banking.model.TransactionType;
import com.system.banking.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    @Mock
    TransactionService transactionService;
    @Mock
    Principal principal;
    @InjectMocks
    TransactionController transactionController;

    @Test
    void shouldBeAbleToPerformTransactionWhenAccountIsCreated() {
        TransactionRequest transactionRequest = new TransactionRequest(TransactionType.CREDIT, new BigDecimal(5));

        transactionController.performTransaction(principal, transactionRequest);

        verify(transactionService).performTransaction(principal.getName(), transactionRequest);
    }

    @Test
    void shouldBeAbleToGetStatement() {
        transactionController.getStatement(principal);

        verify(transactionService).getStatement(principal.getName());
    }
}
