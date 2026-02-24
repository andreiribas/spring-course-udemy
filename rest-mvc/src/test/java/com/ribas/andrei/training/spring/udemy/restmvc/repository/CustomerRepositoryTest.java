package com.ribas.andrei.training.spring.udemy.restmvc.repository;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
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
