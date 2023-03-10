package com.system.banking.view;

import com.system.banking.BankingApplication;
import com.system.banking.model.Customer;
import com.system.banking.repo.AccountRepository;
import com.system.banking.repo.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BankingApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void before() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();

    }

    @AfterEach
    public void after() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();

    }

    @Test
    void shouldBeAbleToLoginSuccessfully() throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Customer customer = new Customer("abc", "abc@gmail.com", "1234", "12345667", "abc", encoder.encode("password"));
        customerRepository.save(customer);

        mockMvc.perform(get("/login")
                        .with(httpBasic("abc@gmail.com", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("abc@gmail.com"));
    }

    @Test
    public void shouldThrowErrorWhenUserCredentialsIsInvalid() throws Exception {
        mockMvc.perform(get("/login")
                        .with((httpBasic("abc@gmail.com", "Password@123"))))
                .andExpect(status().isUnauthorized());
    }
}
