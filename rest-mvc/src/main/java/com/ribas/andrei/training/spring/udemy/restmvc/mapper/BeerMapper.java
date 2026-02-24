package com.ribas.andrei.training.spring.udemy.restmvc.mapper;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer toModel(BeerDTO dto);

    BeerDTO toDTO(Beer model);
}
