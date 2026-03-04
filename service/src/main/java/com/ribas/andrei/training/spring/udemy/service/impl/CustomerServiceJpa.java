package com.ribas.andrei.training.spring.udemy.service.impl;

import com.ribas.andrei.training.spring.udemy.domain.model.Customer;
import com.ribas.andrei.training.spring.udemy.domain.repository.CustomerRepository;
import com.ribas.andrei.training.spring.udemy.service.CustomerService;
import com.ribas.andrei.training.spring.udemy.service.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Customer createCustomer(Customer customer) {
        var now = LocalDateTime.now();
        var newCustomer = Customer.builder()
                .name(customer.getName())
                .createdAt(now)
                .updatedAt(now)
                .build();
        return customerRepository.save(newCustomer);
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Optional<Customer> updateCustomerById(UUID customerId, Customer customer) {
        return getCustomerById(customerId).map(existingCustomer -> {
            customerMapper.updateCustomer(customer, existingCustomer);
            return customerRepository.save(existingCustomer);
        });
    }

    @Override
    public Optional<Customer> deleteCustomerById(UUID customerId) {
        return getCustomerById(customerId).map(existingCustomer -> {
            customerRepository.delete(existingCustomer);
            return existingCustomer;
        });
    }

    @Override
    public Optional<Customer> patchCustomerById(UUID customerId, Customer customer) {
        return getCustomerById(customerId).map(existingCustomer -> {
            customerMapper.patchCustomer(customer, existingCustomer);
            return customerRepository.save(existingCustomer);
        });
    }
}
