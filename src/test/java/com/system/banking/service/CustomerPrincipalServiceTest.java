package com.system.banking.service;

import com.system.banking.controller.request.SignUpRequest;
import com.system.banking.exceptions.EmailIdAlreadyRegisteredException;
import com.system.banking.model.Customer;
import com.system.banking.repo.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerPrincipalServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerPrincipalService customerPrincipalService;


    @Test
    void shouldBeAbleToSaveCustomer() throws EmailIdAlreadyRegisteredException {
        SignUpRequest signUpRequest = new SignUpRequest("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        Customer customer = new Customer("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");

        customerPrincipalService.saveCustomer(signUpRequest);

        verify(customerRepository).save(customer);
    }

    @Test
    void shouldThrowExceptionWhenEmailIdAlreadyExists() throws EmailIdAlreadyRegisteredException {
        Customer customer = new Customer("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        SignUpRequest signUpRequest = new SignUpRequest("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        customerPrincipalService.saveCustomer(signUpRequest);
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        assertThrows(EmailIdAlreadyRegisteredException.class, () -> customerPrincipalService.saveCustomer(signUpRequest));

    }

    @Test
    void shouldBeAbleToGetCustomerWhenEmailIsProvided() throws EmailIdAlreadyRegisteredException {
        SignUpRequest signUpRequest = new SignUpRequest("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        Customer customer = new Customer("abc", "abc@example.com", "1111111111", "123456789", "xyz", "password");
        customerPrincipalService.saveCustomer(signUpRequest);
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        Customer fetchedCustomer = customerPrincipalService.getCustomer(customer.getEmail());

        Assertions.assertEquals(customer, fetchedCustomer);
    }

}
