package com.system.banking.service;

import com.system.banking.controller.request.SignUpRequest;
import com.system.banking.exceptions.EmailIdAlreadyRegisteredException;
import com.system.banking.model.Customer;
import com.system.banking.model.CustomerPrincipal;
import com.system.banking.repo.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerPrincipalService implements UserDetailsService {
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = getCustomer(email);
        return new CustomerPrincipal(customer);
    }

    public Customer getCustomer(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public void saveCustomer(SignUpRequest signUpRequest) throws EmailIdAlreadyRegisteredException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<Customer> fetchedCustomer = customerRepository.findByEmail(signUpRequest.getEmail());
        if(fetchedCustomer.isPresent()) throw new EmailIdAlreadyRegisteredException();
        Customer customer = new Customer(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getMobileNumber(), signUpRequest.getIdentityCard(), signUpRequest.getAddress(), encoder.encode(signUpRequest.getPassword()));
        customerRepository.save(customer);
    }
}
