package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beers";
    public static final String BEER_PATH_WITH_SLASH = BEER_PATH + "/";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Beer> handleNotFoundException(NotFoundException e) {
        log.error("Not found exception: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @PostMapping(BEER_PATH)
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.createBeer(beer);

        String basePath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        URI location = URI.create(basePath + "/" + savedBeer.getId());

//        var httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(location));
//        return new ResponseEntity<>(beer, httpHeaders, HttpStatus.CREATED);

        return ResponseEntity.created(location).body(savedBeer);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<?> updateBeerById(@PathVariable UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId, beer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<?> deleteBeerById(@PathVariable UUID beerId) {
        beerService.deleteBeerById(beerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<?> patchBeerById(@PathVariable UUID beerId, @RequestBody Beer beer) {
        beerService.patchBeerById(beerId, beer);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = BEER_PATH, method = RequestMethod.GET)
    public List<Beer> listBeer() {
        return beerService.listBeers();
    }

    @RequestMapping(value = BEER_PATH_ID, method = RequestMethod.GET)
    public ResponseEntity<Beer> getBeerById(@PathVariable UUID beerId) {
        log.debug("Get beer by id: {} - in controller.", beerId);
        var beer = beerService.getBeerById(beerId);
        return ResponseEntity.ok(beer);
    }
}
