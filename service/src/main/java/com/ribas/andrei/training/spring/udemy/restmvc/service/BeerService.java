package com.ribas.andrei.training.spring.udemy.restmvc.service;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Beer createBeer(Beer beer);

    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID beerId);

    Optional<Beer> updateBeerById(UUID beerId, Beer beer);

    Optional<Beer> deleteBeerById(UUID beerId);

    Optional<Beer> patchBeerById(UUID beerId, Beer beer);
}
