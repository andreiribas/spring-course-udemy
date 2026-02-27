package com.ribas.andrei.training.spring.udemy.service;

import com.ribas.andrei.training.spring.udemy.domain.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Customer createCustomer(Customer customer);

    List<Customer> listCustomers();

    Optional<Customer> getCustomerById(UUID customerId);

    Optional<Customer> updateCustomerById(UUID customerId, Customer customer);

    Optional<Customer> deleteCustomerById(UUID customerId);

    Optional<Customer> patchCustomerById(UUID customerId, Customer customer);
}
