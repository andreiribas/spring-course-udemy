package com.ribas.andrei.training.spring.udemy.service.impl;

import com.ribas.andrei.training.spring.udemy.domain.model.Customer;
import com.ribas.andrei.training.spring.udemy.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, Customer> customersMap;

    public CustomerServiceImpl() {
        this.customersMap = new ConcurrentHashMap<>();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        var newCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .name(customer.getName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        this.customersMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }

    @Override
    public List<Customer> listCustomers() {
        return customersMap.values().stream().toList();
    }

    @Override
    public Optional<Customer> getCustomerById(UUID customerId) {
        return Optional.ofNullable(customersMap.get(customerId));
    }

    @Override
    public Optional<Customer> updateCustomerById(UUID customerId, Customer customer) {
        var updatedCustomer = customersMap.get(customerId);
        if(updatedCustomer == null) {
            return Optional.empty();
        }
        updatedCustomer.setName(customer.getName());
        updatedCustomer.setUpdatedAt(LocalDateTime.now());
        return Optional.of(updatedCustomer);
    }

    @Override
    public Optional<Customer> deleteCustomerById(UUID customerId) {
        var deletedCustomer = customersMap.remove(customerId);
        return Optional.ofNullable(deletedCustomer);
    }

    @Override
    public Optional<Customer> patchCustomerById(UUID customerId, Customer customer) {
        var updatedCustomer = customersMap.get(customerId);
        if(updatedCustomer == null) {
            return Optional.empty();
        }
        boolean wasUpdated = false;
        if(customer.getName() != null) {
            updatedCustomer.setName(customer.getName());
            wasUpdated = true;
        }
        if(wasUpdated) {
            updatedCustomer.setUpdatedAt(LocalDateTime.now());
        }
        return Optional.of(updatedCustomer);
    }
}
