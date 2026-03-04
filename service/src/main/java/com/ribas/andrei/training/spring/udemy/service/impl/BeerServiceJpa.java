package com.ribas.andrei.training.spring.udemy.service.impl;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.domain.repository.BeerRepository;
import com.ribas.andrei.training.spring.udemy.service.BeerService;
import com.ribas.andrei.training.spring.udemy.service.mapper.BeerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Beer createBeer(Beer beer) {
        var now = LocalDateTime.now();
        var newBeer = Beer.builder()
                .name(beer.getName())
                .style(beer.getStyle())
                .quantity(beer.getQuantity())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .createdAt(now)
                .updatedAt(now)
                .build();
        return beerRepository.save(newBeer);
    }

    @Override
    public List<Beer> listBeers() {
        return beerRepository.findAll();
    }

    @Override
    public Optional<Beer> getBeerById(UUID beerId) {
        return beerRepository.findById(beerId);
    }

    @Override
    public Optional<Beer> updateBeerById(UUID beerId, Beer beer) {
        return getBeerById(beerId).map(existingBeer -> {
            beerMapper.updateBeer(beer, existingBeer);
            return beerRepository.save(existingBeer);
        });
    }

    @Override
    public Optional<Beer> deleteBeerById(UUID beerId) {
        return getBeerById(beerId).map(existingBeer -> {
            beerRepository.delete(existingBeer);
            return existingBeer;
        });
    }

    @Override
    public Optional<Beer> patchBeerById(UUID beerId, Beer beer) {
        return getBeerById(beerId).map(existingBeer -> {
            beerMapper.patchBeer(beer, existingBeer);
            return beerRepository.save(existingBeer);
        });
    }
}
