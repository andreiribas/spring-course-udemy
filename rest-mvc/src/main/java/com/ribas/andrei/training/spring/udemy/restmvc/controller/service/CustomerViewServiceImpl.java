package com.ribas.andrei.training.spring.udemy.restmvc.controller.service;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.mapper.CustomerMapper;
import com.ribas.andrei.training.spring.udemy.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerViewServiceImpl implements CustomerViewService {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        return customerMapper.toDTO(customerService.createCustomer(customerMapper.toModel(customer)));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers().stream().map(customerMapper::toDTO).toList();
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return customerService.getCustomerById(customerId).map(customerMapper::toDTO);
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        return customerService.updateCustomerById(customerId, customerMapper.toModel(customer)).map(customerMapper::toDTO);
    }

    @Override
    public Optional<CustomerDTO> deleteCustomerById(UUID customerId) {
        return customerService.deleteCustomerById(customerId).map(customerMapper::toDTO);
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        return customerService.patchCustomerById(customerId, customerMapper.toModel(customer)).map(customerMapper::toDTO);
    }
}
