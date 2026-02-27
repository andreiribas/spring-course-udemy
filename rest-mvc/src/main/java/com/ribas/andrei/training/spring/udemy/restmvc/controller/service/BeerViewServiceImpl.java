package com.ribas.andrei.training.spring.udemy.restmvc.controller.service;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.mapper.BeerMapper;
import com.ribas.andrei.training.spring.udemy.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerViewServiceImpl implements BeerViewService {

    private final BeerService beerService;
    private final BeerMapper beerMapper;

    @Override
    public BeerDTO createBeer(BeerDTO beer) {
        return beerMapper.toDTO(beerService.createBeer(beerMapper.toModel(beer)));
    }

    @Override
    public List<BeerDTO> listBeers() {
        return beerService.listBeers().stream().map(beerMapper::toDTO).toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID beerId) {
        return beerService.getBeerById(beerId).map(beerMapper::toDTO);
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        return beerService.updateBeerById(beerId, beerMapper.toModel(beer)).map(beerMapper::toDTO);
    }

    @Override
    public Optional<BeerDTO> deleteBeerById(UUID beerId) {
        return beerService.deleteBeerById(beerId).map(beerMapper::toDTO);
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        return beerService.patchBeerById(beerId, beerMapper.toModel(beer)).map(beerMapper::toDTO);
    }
}
