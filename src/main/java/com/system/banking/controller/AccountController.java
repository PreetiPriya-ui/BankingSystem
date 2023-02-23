package com.system.banking.controller;

import com.system.banking.controller.request.SignUpRequest;
import com.system.banking.exceptions.EmailIdAlreadyRegisteredException;
import com.system.banking.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;

    @PostMapping("/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void save(@RequestBody SignUpRequest signupRequest) throws IOException, EmailIdAlreadyRegisteredException {
        accountService.createAccount(signupRequest);
    }

    @GetMapping("/summary")
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, Object> getSummary(Principal principal) {
       return accountService.getSummary(principal.getName());
    }


}
