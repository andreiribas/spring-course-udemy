package com.ribas.andrei.training.spring.udemy.restmvc.mapper;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateBeerDTO;
import org.mapstruct.*;

@Mapper
public interface BeerDTOMapper {

    Beer toModel(CreateOrUpdateBeerDTO dto);

    BeerDTO toDTO(Beer model);

}
