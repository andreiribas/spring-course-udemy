package com.ribas.andrei.training.spring.udemy.restmvc.controller.service;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerViewService {
    CustomerDTO createCustomer(CustomerDTO customer);

    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID customerId);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer);

    Optional<CustomerDTO> deleteCustomerById(UUID customerId);

    Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer);
}
