package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    @PostMapping
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.createBeer(beer);

//        var httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(URI.create("/api/v1/beers/" + savedBeer.getId()));
//        return new ResponseEntity<>(beer, httpHeaders, HttpStatus.CREATED);

        return ResponseEntity.created(URI.create("/api/v1/beers/" + savedBeer.getId())).body(savedBeer);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeer() {
        return beerService.listBeers();
    }

    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public ResponseEntity<Beer> getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("Get beer by id: {} - in controller.", beerId);
        var beer = beerService.getBeerById(beerId);
        if(beer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(beer);
    }
}
