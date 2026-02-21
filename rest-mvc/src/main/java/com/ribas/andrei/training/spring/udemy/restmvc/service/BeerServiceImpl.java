package com.ribas.andrei.training.spring.udemy.restmvc.service;

import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BeerServiceImpl implements BeerService {
    public static final String BEER_NOT_FOUND_MESSAGE = "Beer with id %s not found";
    private final Map<UUID, Beer> beersMap;

    public BeerServiceImpl() {
        this.beersMap = new ConcurrentHashMap<>();
    }

    @Override
    public Beer createBeer(Beer beer) {
        var newBeer = Beer.builder()
                .id(UUID.randomUUID())
                .name(beer.getName())
                .style(beer.getStyle())
                .quantity(beer.getQuantity())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        this.beersMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }

    @Override
    public List<Beer> listBeers() {
        return beersMap.values().stream().toList();
    }

    @Override
    public Beer getBeerById(UUID beerId) {
        var beer = beersMap.get(beerId);
        throwBeerNotFoundExceptionIfNull(beer, beerId);
        return beer;
    }

    @Override
    public Beer updateBeerById(UUID beerId, Beer beer) {
        var updatedBeer = beersMap.get(beerId);
        throwBeerNotFoundExceptionIfNull(updatedBeer, beerId);

        if(beer != null) {
            updatedBeer.setName(beer.getName());
            updatedBeer.setStyle(beer.getStyle());
            updatedBeer.setQuantity(beer.getQuantity());
            updatedBeer.setUpc(beer.getUpc());
            updatedBeer.setPrice(beer.getPrice());
            updatedBeer.setUpdatedAt(LocalDateTime.now());
        }
        return updatedBeer;
    }

    @Override
    public Beer deleteBeerById(UUID beerId) {
        var deletedBeer = beersMap.remove(beerId);
        throwBeerNotFoundExceptionIfNull(deletedBeer, beerId);
        return deletedBeer;
    }

    @Override
    public Beer patchBeerById(UUID beerId, Beer beer) {
        var updatedBeer = beersMap.get(beerId);
        throwBeerNotFoundExceptionIfNull(updatedBeer, beerId);
        if(beer != null) {
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
        }
        return updatedBeer;
    }

    private void throwBeerNotFoundExceptionIfNull(Beer updatedBeer, UUID beerId) {
        if(updatedBeer == null) {
            throw new NotFoundException(BEER_NOT_FOUND_MESSAGE.formatted(beerId));
        }
    }
}
