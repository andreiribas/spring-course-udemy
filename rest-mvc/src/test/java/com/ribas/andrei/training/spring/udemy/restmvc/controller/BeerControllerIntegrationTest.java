package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.domain.repository.BeerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.RestMvcSpringBootApplication;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateBeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = RestMvcSpringBootApplication.class)
class BeerControllerIntegrationTest {

    @Autowired
    private BeerController fixture;

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void testListBeersWhenThereAreBeersShouldReturnListWithValues() {
        var response = fixture.listBeers();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(3, response.getBody().size());
    }

    @Rollback
    @Transactional
    @Test
    void testListBeersWhenThereAreNoBeersShouldReturnEmptyListAndOKStatus() {
        beerRepository.deleteAll();

        var response = fixture.listBeers();
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() {
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.getBeerById(id));
        assertEquals("Beer with id %s not found".formatted(id), exception.getMessage());
    }

    @Test
    void testGetBeerByIdExistsShouldReturnItWithStatusOKStatus() {
        var id = getFirstBeerFromList().getId();

        var response = fixture.getBeerById(id);
        assertEquals(200, response.getStatusCode().value());
        var customer = response.getBody();
        assertNotNull(customer);
        assertEquals(id, customer.getId());
    }

    @Test
    void testCreateNewBeerShouldWorkAndReturnCreatedStatus() {
        var newBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Jupiler")
                .style("Pilsen")
                .quantity(48)
                .upc("7561238480")
                .price(new BigDecimal("3.5"))
                .build();
        var createdBeerResponse = fixture.createBeer(newBeerDTO);
        assertNotNull(createdBeerResponse.getHeaders().getLocation());
        var idFromLocation = UUID.fromString(createdBeerResponse.getHeaders().getLocation().getPath().split("/")[1]);
        var createdBeer = createdBeerResponse.getBody();
        assertNotNull(createdBeer);
        assertEquals(idFromLocation, createdBeer.getId());
        assertEquals(newBeerDTO.getName(), createdBeer.getName());
        assertEquals(newBeerDTO.getStyle(), createdBeer.getStyle());
        assertEquals(newBeerDTO.getQuantity(), createdBeer.getQuantity());
        assertEquals(newBeerDTO.getUpc(), createdBeer.getUpc());
        assertEquals(newBeerDTO.getPrice(), createdBeer.getPrice());
        assertNotNull(createdBeer.getCreatedAt());
        assertNotNull(createdBeer.getUpdatedAt());
        assertEquals(createdBeer.getCreatedAt(), createdBeer.getUpdatedAt());

        assertTrue(beerRepository.existsById(idFromLocation));
    }

    @Test
    void testUpdateBeerByIdWhenItDoesNotExistShouldReturnNotFound() {
        var updatedBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.updateBeerById(id, updatedBeerDTO));
        assertEquals("Beer with id %s not found".formatted(id), exception.getMessage());
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateBeerByIdWhenItExistsShouldReturnOK() {
        var updatedBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();
        var id = getFirstBeerFromList().getId();
        var response = fixture.updateBeerById(id, updatedBeerDTO);
        assertEquals(204, response.getStatusCode().value());

        var updatedBeer = beerRepository.findById(id).orElseThrow();
        assertNotNull(updatedBeer);
        assertEquals(updatedBeerDTO.getName(), updatedBeer.getName());
        assertEquals(updatedBeerDTO.getStyle(), updatedBeer.getStyle());
        assertEquals(updatedBeerDTO.getQuantity(), updatedBeer.getQuantity());
        assertEquals(updatedBeerDTO.getUpc(), updatedBeer.getUpc());
        assertEquals(0, updatedBeerDTO.getPrice().compareTo(updatedBeer.getPrice()));
        assertNotNull(updatedBeer.getCreatedAt());
        assertNotNull(updatedBeer.getUpdatedAt());
        assertTrue(updatedBeer.getCreatedAt().isBefore(updatedBeer.getUpdatedAt()));
    }

    @Test
    void testPatchBeerByIdWhenItDoesNotExistShouldReturnNotFound() {
        var patchedBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Patched Maes")
                .build();
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.patchBeerById(id, patchedBeerDTO));
        assertEquals("Beer with id %s not found".formatted(id), exception.getMessage());
    }

    @Rollback
    @Transactional
    @Test
    void testPatchBeerByIdWhenItExistsShouldReturnNoResponse() {
        var updatedBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Patched Maes")
                .build();
        var id = getFirstBeerFromList().getId();
        var response = fixture.patchBeerById(id, updatedBeerDTO);
        assertEquals(204, response.getStatusCode().value());

        var updatedBeer = beerRepository.findById(id).orElseThrow();
        assertNotNull(updatedBeer);
        assertEquals(updatedBeerDTO.getName(), updatedBeer.getName());
        assertNotEquals(updatedBeerDTO.getStyle(), updatedBeer.getStyle());
        assertNotEquals(updatedBeerDTO.getQuantity(), updatedBeer.getQuantity());
        assertNotEquals(updatedBeerDTO.getUpc(), updatedBeer.getUpc());
        assertNotEquals(updatedBeer.getPrice(), updatedBeerDTO.getPrice());
        assertNotNull(updatedBeer.getCreatedAt());
        assertNotNull(updatedBeer.getUpdatedAt());
        assertTrue(updatedBeer.getCreatedAt().isBefore(updatedBeer.getUpdatedAt()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchBeerByIdWhenItExistsAndAllFieldsAreNullShouldReturnNoResponse() {
        var updatedBeerDTO = CreateOrUpdateBeerDTO.builder()
                .build();
        var id = getFirstBeerFromList().getId();
        var response = fixture.patchBeerById(id, updatedBeerDTO);
        assertEquals(204, response.getStatusCode().value());

        var updatedBeer = beerRepository.findById(id).orElseThrow();
        assertNotNull(updatedBeer);
        assertNotEquals(updatedBeerDTO.getName(), updatedBeer.getName());
        assertNotEquals(updatedBeerDTO.getStyle(), updatedBeer.getStyle());
        assertNotEquals(updatedBeerDTO.getQuantity(), updatedBeer.getQuantity());
        assertNotEquals(updatedBeerDTO.getUpc(), updatedBeer.getUpc());
        assertNotEquals(updatedBeer.getPrice(), updatedBeerDTO.getPrice());
        assertNotNull(updatedBeer.getCreatedAt());
        assertNotNull(updatedBeer.getUpdatedAt());
        assertTrue(updatedBeer.getCreatedAt().isBefore(updatedBeer.getUpdatedAt()));
    }

    @Test
    void testDeleteBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.deleteBeerById(id));
        assertEquals("Beer with id %s not found".formatted(id), exception.getMessage());
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteBeerByIdExistsShouldDoNothing() throws Exception {
        var beerId = getFirstBeerFromList().getId();
        fixture.deleteBeerById(beerId);
        var exception = assertThrows(NotFoundException.class, () -> fixture.getBeerById(beerId));
        assertEquals("Beer with id %s not found".formatted(beerId), exception.getMessage());
    }

    private BeerDTO getFirstBeerFromList() {
        return fixture.listBeers().getBody().getFirst();
    }
}
