package com.ribas.andrei.training.spring.udemy.restmvc.service;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    Beer createBeer(Beer beer);

    List<Beer> listBeers();

    Beer getBeerById(UUID beerId);
}
