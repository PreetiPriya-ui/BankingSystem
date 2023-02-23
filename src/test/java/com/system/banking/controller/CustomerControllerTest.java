package com.system.banking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    @InjectMocks
    CustomerController customerController;
    @Mock
    Principal principal;

    @Test
    void shouldBeAbleToLoginSuccessfully() {
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("email", principal.getName());

        Map<String, Object> actualResponse = customerController.login(principal);

        assertThat(actualResponse, is(expectedResponse));
    }
}
