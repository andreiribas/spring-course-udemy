package com.ribas.andrei.training.spring.udemy.restmvc.mapper;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer toModel(BeerDTO dto);

    BeerDTO toDTO(Beer model);
}
