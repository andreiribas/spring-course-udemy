package com.ribas.andrei.training.spring.udemy.restmvc.service;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BeerServiceImpl implements BeerService {
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
        return beersMap.get(beerId);
    }

    @Override
    public Beer updateBeerById(UUID beerId, Beer beer) {
        var updatedBeer = beersMap.get(beerId);
        if(updatedBeer != null && //
                beer != null) {
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
        return beersMap.remove(beerId);
    }
}
