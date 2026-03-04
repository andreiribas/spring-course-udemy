package com.ribas.andrei.training.spring.udemy.restmvc.controller.service;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateBeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.mapper.BeerDTOMapper;
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
    private final BeerDTOMapper beerDTOMapper;

    @Override
    public BeerDTO createBeer(CreateOrUpdateBeerDTO beer) {
        return beerDTOMapper.toDTO(beerService.createBeer(beerDTOMapper.toModel(beer)));
    }

    @Override
    public List<BeerDTO> listBeers() {
        return beerService.listBeers().stream().map(beerDTOMapper::toDTO).toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID beerId) {
        return beerService.getBeerById(beerId).map(beerDTOMapper::toDTO);
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, CreateOrUpdateBeerDTO beer) {
        return beerService.updateBeerById(beerId, beerDTOMapper.toModel(beer)).map(beerDTOMapper::toDTO);
    }

    @Override
    public Optional<BeerDTO> deleteBeerById(UUID beerId) {
        return beerService.deleteBeerById(beerId).map(beerDTOMapper::toDTO);
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, CreateOrUpdateBeerDTO beer) {
        return beerService.patchBeerById(beerId, beerDTOMapper.toModel(beer)).map(beerDTOMapper::toDTO);
    }
}
