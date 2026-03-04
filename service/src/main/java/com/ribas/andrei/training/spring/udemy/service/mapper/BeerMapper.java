package com.ribas.andrei.training.spring.udemy.service.mapper;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BeerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateBeer(Beer beer, @MappingTarget Beer target);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchBeer(Beer beer, @MappingTarget Beer target);
}
