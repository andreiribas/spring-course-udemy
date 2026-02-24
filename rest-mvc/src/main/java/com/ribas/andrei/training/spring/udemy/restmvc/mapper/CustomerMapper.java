package com.ribas.andrei.training.spring.udemy.restmvc.mapper;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.model.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer toModel(CustomerDTO dto);

    CustomerDTO toDTO(Customer model);
}
