package com.system.banking.controller;

import com.system.banking.controller.request.TransactionRequest;
import com.system.banking.controller.response.TransactionStatementResponse;
import com.system.banking.exceptions.AccountNumberNotFoundException;
import com.system.banking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping
@AllArgsConstructor
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping("/transaction")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void performTransaction(Principal principal, @RequestBody TransactionRequest transactionRequest) {
        transactionService.performTransaction(principal.getName(), transactionRequest);

    }

    @GetMapping("/statement")
    @ResponseStatus(code = HttpStatus.OK)
    public TransactionStatementResponse getStatement(Principal principal) {
        return transactionService.getStatement(principal.getName());

    }
}
