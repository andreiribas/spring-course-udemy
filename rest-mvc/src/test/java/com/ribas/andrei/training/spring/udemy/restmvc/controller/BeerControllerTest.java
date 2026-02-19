package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BeerService beerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListBeerWhenThereAreNoBeersShouldReturnEmptyList() throws Exception {
        given(beerService.listBeers())
                .willReturn(Collections.emptyList());
        mockMvc.perform(
                        get("/api/v1/beers")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));
        verify(beerService, times(1)).listBeers();
    }

    @Test
    void testListBeerWhenThereAreBeersShouldReturnList() throws Exception {
        var beers = List.of(createDefaultBeer(UUID.randomUUID()),
                createDefaultBeer(UUID.randomUUID()));
        given(beerService.listBeers())
                .willReturn(beers);
        mockMvc.perform(
                        get("/api/v1/beers")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));

        verify(beerService, times(1)).listBeers();
    }

    @Test
    void testCreateNewBeerShouldWork() throws Exception {
        Beer beer = Beer.builder()
                .name("Jupiler")
                .style("Pilsen")
                .quantity(48)
                .upc("7561238480")
                .price(new BigDecimal("3.5")).build();

        given(beerService.createBeer(any(Beer.class))).willAnswer(a -> a.getArgument(0, Beer.class));
        mockMvc.perform(
                        post("/api/v1/beers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        verify(beerService, times(1)).createBeer(any(Beer.class));
    }

    @Test
    void testUpdateBeerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
        Beer beer = Beer.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6")).build();
        given(beerService.updateBeerById(any(UUID.class), any(Beer.class))).willReturn(null);
        mockMvc.perform(
                        put("/api/v1/beers/" + UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isNotFound());
        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testUpdateBeerByIdWhenItExistsShouldReturnOK() throws Exception {
        Beer beer = Beer.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6")).build();
        given(beerService.updateBeerById(any(UUID.class), any(Beer.class))).willAnswer(a -> a.getArgument(1, Beer.class));
        mockMvc.perform(
                        put("/api/v1/beers/" + UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isNoContent());
        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void tesPatchBeerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
        Beer beer = Beer.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6")).build();
        given(beerService.patchBeerById(any(UUID.class), any(Beer.class))).willReturn(null);
        mockMvc.perform(
                        patch("/api/v1/beers/" + UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isNotFound());
        verify(beerService, times(1)).patchBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testPatchBeerByIdWhenItExistsShouldReturnOK() throws Exception {
        Beer beer = Beer.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6")).build();
        given(beerService.patchBeerById(any(UUID.class), any(Beer.class))).willAnswer(a -> a.getArgument(1, Beer.class));
        mockMvc.perform(
                        patch("/api/v1/beers/" + UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isNoContent());
        verify(beerService, times(1)).patchBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testDeleteBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/beers/" + UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBeerByIdExistsShouldDoNothing() throws Exception {
        var beerId = UUID.randomUUID();
        var beer = createDefaultBeer(beerId);
        given(beerService.deleteBeerById(beerId))
                .willReturn(beer);
        mockMvc.perform(
                        delete("/api/v1/beers/" + beerId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        verify(beerService, times(1)).deleteBeerById(beerId);
    }

    @Test
    void testGetBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
        mockMvc.perform(
                get("/api/v1/beers/" + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetBeerByIdExistsShouldReturnIt() throws Exception {
        var beerId = UUID.randomUUID();
        var beer = createDefaultBeer(beerId);
        given(beerService.getBeerById(beerId))
            .willReturn(beer);
        mockMvc.perform(
                        get("/api/v1/beers/" + beerId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(beerId.toString())))
            .andExpect(jsonPath("$.name", is(beer.getName())));
        verify(beerService, times(1)).getBeerById(beerId);
    }

    private Beer createDefaultBeer(UUID beerId) {
        var now = LocalDateTime.now();
        return Beer.builder()
                .id(beerId)
                .name("Test")
                .style("Stout")
                .quantity(147)
                .upc("459675121340")
                .price(new BigDecimal("2.47"))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

}
