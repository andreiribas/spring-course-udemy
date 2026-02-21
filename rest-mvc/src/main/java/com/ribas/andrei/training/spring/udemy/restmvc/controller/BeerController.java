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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

        String basePath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        URI location = URI.create(basePath + "/" + savedBeer.getId());

//        var httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(location));
//        return new ResponseEntity<>(beer, httpHeaders, HttpStatus.CREATED);

        return ResponseEntity.created(location).body(savedBeer);
    }

    @PutMapping("{beerId}")
    public ResponseEntity<?> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        Beer updatedBeer = beerService.updateBeerById(beerId, beer);
        if(updatedBeer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{beerId}")
    public ResponseEntity<?> deleteBeerById(@PathVariable("beerId") UUID beerId) {
        Beer deletedBeer = beerService.deleteBeerById(beerId);
        if(deletedBeer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{beerId}")
    public ResponseEntity<?> patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        Beer patchedBeer = beerService.patchBeerById(beerId, beer);
        if(patchedBeer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
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
