package com.system.banking.controller;

import com.system.banking.controller.request.SignUpRequest;
import com.system.banking.exceptions.EmailIdAlreadyRegisteredException;
import com.system.banking.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.Principal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Mock
    Principal principal;

    @Mock
    AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void shouldBeAbleToCreateAccountSuccessfully() throws IOException, EmailIdAlreadyRegisteredException {
        SignUpRequest signupRequest = new SignUpRequest("abc", "abc@example.com", "9999999999", "011", "bihar", "preeti@123");

        accountController.save(signupRequest);

        verify(accountService).createAccount(signupRequest);
    }

    @Test
    void shouldBeAbleToGetSummaryOfParticularCustomer() {
        accountController.getSummary(principal);

        verify(accountService).getSummary(principal.getName());
    }
}
