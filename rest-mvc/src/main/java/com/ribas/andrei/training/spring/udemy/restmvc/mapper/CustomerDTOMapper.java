package com.ribas.andrei.training.spring.udemy.restmvc.mapper;

import com.ribas.andrei.training.spring.udemy.domain.model.Customer;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateCustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;

import org.mapstruct.Mapper;

@Mapper
public interface CustomerDTOMapper {

    Customer toModel(CreateOrUpdateCustomerDTO dto);

    CustomerDTO toDTO(Customer model);
}
