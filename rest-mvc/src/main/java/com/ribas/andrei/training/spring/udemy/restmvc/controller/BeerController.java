package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.controller.service.BeerViewService;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.ribas.andrei.training.spring.udemy.service.BeerServiceImpl.BEER_NOT_FOUND_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beers";
    public static final String BEER_PATH_WITH_SLASH = BEER_PATH + "/";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerViewService beerViewService;

    private Supplier<NotFoundException> createThrowBeerNotFoundExceptionSupplier(UUID beerId) {
        return () -> new NotFoundException(BEER_NOT_FOUND_MESSAGE.formatted(beerId));
    }

    @PostMapping(BEER_PATH)
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BeerDTO> createBeer(@RequestBody BeerDTO beer) {

        BeerDTO savedBeer = beerViewService.createBeer(beer);

        String basePath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        URI location = URI.create(basePath + "/" + savedBeer.getId());

//        var httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(location));
//        return new ResponseEntity<>(beer, httpHeaders, HttpStatus.CREATED);

        return ResponseEntity.created(location).body(savedBeer);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<?> updateBeerById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        beerViewService.updateBeerById(beerId, beer).orElseThrow(createThrowBeerNotFoundExceptionSupplier(beerId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<?> deleteBeerById(@PathVariable UUID beerId) {
        beerViewService.deleteBeerById(beerId).orElseThrow(createThrowBeerNotFoundExceptionSupplier(beerId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<?> patchBeerById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        beerViewService.patchBeerById(beerId, beer).orElseThrow(createThrowBeerNotFoundExceptionSupplier(beerId));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = BEER_PATH, method = RequestMethod.GET)
    public List<BeerDTO> listBeer() {
        return beerViewService.listBeers();
    }

    @RequestMapping(value = BEER_PATH_ID, method = RequestMethod.GET)
    public ResponseEntity<BeerDTO> getBeerById(@PathVariable UUID beerId) {
        log.debug("Get beer by id: {} - in controller.", beerId);
        var beer = beerViewService.getBeerById(beerId).orElseThrow(createThrowBeerNotFoundExceptionSupplier(beerId));
        return ResponseEntity.ok(beer);
    }
}
