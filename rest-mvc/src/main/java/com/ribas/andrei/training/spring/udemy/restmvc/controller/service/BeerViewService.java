package com.ribas.andrei.training.spring.udemy.restmvc.controller.service;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateBeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerViewService {
    BeerDTO createBeer(CreateOrUpdateBeerDTO beer);

    List<BeerDTO> listBeers();

    Optional<BeerDTO> getBeerById(UUID beerId);

    Optional<BeerDTO> updateBeerById(UUID beerId, CreateOrUpdateBeerDTO beer);

    Optional<BeerDTO> deleteBeerById(UUID beerId);

    Optional<BeerDTO> patchBeerById(UUID beerId, CreateOrUpdateBeerDTO beer);
}
