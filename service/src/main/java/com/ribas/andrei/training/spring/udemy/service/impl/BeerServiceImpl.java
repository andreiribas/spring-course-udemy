package com.ribas.andrei.training.spring.udemy.service.impl;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.service.BeerService;
import com.ribas.andrei.training.spring.udemy.service.mapper.BeerMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BeerServiceImpl implements BeerService {
    private final Map<UUID, Beer> beersMap;

    private final BeerMapper beerMapper;

    public BeerServiceImpl(
            BeerMapper beerMapper) {
        this.beerMapper = beerMapper;
        this.beersMap = new ConcurrentHashMap<>();
    }

    @Override
    public Beer createBeer(Beer newBeer) {
        newBeer.setId(UUID.randomUUID());
        newBeer.setCreatedAt(LocalDateTime.now());
        newBeer.setUpdatedAt(LocalDateTime.now());
        this.beersMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }

    @Override
    public List<Beer> listBeers() {
        return beersMap.values().stream().toList();
    }

    @Override
    public Optional<Beer> getBeerById(UUID beerId) {
        return getBeerByIdInternal(beerId);
    }

    @Override
    public Optional<Beer> updateBeerById(UUID beerId, Beer beer) {
        return getBeerByIdInternal(beerId).map(updatedBeer -> {
            beerMapper.updateBeer(beer, updatedBeer);
            return updatedBeer;
        });
    }

    @Override
    public Optional<Beer> deleteBeerById(UUID beerId) {
        var deletedBeer = beersMap.remove(beerId);
        return Optional.ofNullable(deletedBeer);
    }

    @Override
    public Optional<Beer> patchBeerById(UUID beerId, Beer beer) {
        var updatedBeer = beersMap.get(beerId);
        if(updatedBeer == null) {
            return Optional.empty();
        }
        boolean wasUpdated = false;
        if(beer.getName() != null) {
            updatedBeer.setName(beer.getName());
            wasUpdated = true;
        }
        if(beer.getStyle() != null) {
            updatedBeer.setStyle(beer.getStyle());
            wasUpdated = true;
        }
        if(beer.getQuantity() != null) {
            updatedBeer.setQuantity(beer.getQuantity());
            wasUpdated = true;
        }
        if(beer.getUpc() != null) {
            updatedBeer.setUpc(beer.getUpc());
            wasUpdated = true;
        }
        if(beer.getPrice() != null) {
            updatedBeer.setPrice(beer.getPrice());
            wasUpdated = true;
        }
        if(wasUpdated) {
            updatedBeer.setUpdatedAt(LocalDateTime.now());
        }
        return Optional.of(updatedBeer);
    }

    private Optional<Beer> getBeerByIdInternal(UUID id) {
        return Optional.ofNullable(beersMap.get(id));
    }
}
