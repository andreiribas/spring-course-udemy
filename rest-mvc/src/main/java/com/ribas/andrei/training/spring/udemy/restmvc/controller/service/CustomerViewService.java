package com.ribas.andrei.training.spring.udemy.restmvc.controller.service;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateCustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerViewService {
    CustomerDTO createCustomer(CreateOrUpdateCustomerDTO customer);

    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID customerId);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CreateOrUpdateCustomerDTO customer);

    Optional<CustomerDTO> deleteCustomerById(UUID customerId);

    Optional<CustomerDTO> patchCustomerById(UUID customerId, CreateOrUpdateCustomerDTO customer);
}
