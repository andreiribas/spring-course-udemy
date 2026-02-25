package com.ribas.andrei.training.spring.udemy.domain.repository;

import com.ribas.andrei.training.spring.udemy.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTestImpl
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testSave() {
        var customer = customerRepository.save(Customer.builder()
                .name("Test Customer")
                .build());
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

}
