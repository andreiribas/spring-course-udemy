package com.ribas.andrei.training.spring.udemy.restmvc.controller.service;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateCustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.mapper.CustomerDTOMapper;
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
    private final CustomerDTOMapper customerDTOMapper;

    @Override
    public CustomerDTO createCustomer(CreateOrUpdateCustomerDTO customer) {
        return customerDTOMapper.toDTO(customerService.createCustomer(customerDTOMapper.toModel(customer)));
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerService.listCustomers().stream().map(customerDTOMapper::toDTO).toList();
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return customerService.getCustomerById(customerId).map(customerDTOMapper::toDTO);
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CreateOrUpdateCustomerDTO customer) {
        return customerService.updateCustomerById(customerId, customerDTOMapper.toModel(customer)).map(customerDTOMapper::toDTO);
    }

    @Override
    public Optional<CustomerDTO> deleteCustomerById(UUID customerId) {
        return customerService.deleteCustomerById(customerId).map(customerDTOMapper::toDTO);
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CreateOrUpdateCustomerDTO customer) {
        return customerService.patchCustomerById(customerId, customerDTOMapper.toModel(customer)).map(customerDTOMapper::toDTO);
    }
}
