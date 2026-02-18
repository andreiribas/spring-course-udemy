package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.service.BeerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BeerService beerService;

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
