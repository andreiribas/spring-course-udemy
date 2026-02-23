package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.service.BeerService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.ribas.andrei.training.spring.udemy.restmvc.controller.BeerController.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                    get(BEER_PATH)
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
                    get(BEER_PATH)
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

        given(beerService.createBeer(any(Beer.class))).willAnswer(a -> {
            var newBeer = a.getArgument(0, Beer.class);
            newBeer.setId(UUID.randomUUID());
            return newBeer;
        });
        var beerArgumentCaptor = ArgumentCaptor.forClass(Beer.class);
        var mvcResult = mockMvc.perform(
                    post(BEER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isCreated())
                .andReturn();
        verify(beerService, times(1)).createBeer(beerArgumentCaptor.capture());

        var capturedBeer = beerArgumentCaptor.getValue();
        // assert Location header contains the captured id
        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals(BEER_PATH_WITH_SLASH + capturedBeer.getId(), location);
    }

    @Test
    void testUpdateBeerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
        Beer beer = Beer.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6")).build();
        given(beerService.updateBeerById(any(UUID.class), any(Beer.class))).willReturn(Optional.empty());
        mockMvc.perform(
                    put(BEER_PATH_ID, UUID.randomUUID())
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
        given(beerService.updateBeerById(any(UUID.class), any(Beer.class))).willAnswer(a -> Optional.of(a.getArgument(1, Beer.class)));
        mockMvc.perform(
                    put(BEER_PATH_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isNoContent());
        verify(beerService, times(1)).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testPatchBeerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
        Beer beer = Beer.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6")).build();
        given(beerService.patchBeerById(any(UUID.class), any(Beer.class))).willThrow(new NotFoundException("Beer not found"));
        mockMvc.perform(
                    patch(BEER_PATH_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer))
                )
                .andExpect(status().isNotFound());
        verify(beerService, times(1)).patchBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void testPatchBeerByIdWhenItExistsShouldReturnOK() throws Exception {

        Map<String, Object> beerFields = new HashMap<>();
        var beerName = "Maes";
        var beerStyle = "Pilsen";
        beerFields.put("name", beerName);
        beerFields.put("style", beerStyle);

        var beerIdArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        var beerArgumentCaptor = ArgumentCaptor.forClass(Beer.class);

        var beerId = UUID.randomUUID();
        given(beerService.patchBeerById(eq(beerId), any(Beer.class))).willAnswer(a -> Optional.of(a.getArgument(1, Beer.class)));
        mockMvc.perform(
                    patch(BEER_PATH_ID, beerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerFields))
                )
                .andExpect(status().isNoContent());
        verify(beerService, times(1)).patchBeerById(beerIdArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beerArgumentCaptor.getValue().getName()).isEqualTo(beerName);
        assertThat(beerArgumentCaptor.getValue().getStyle()).isEqualTo(beerStyle);
        assertThat(beerIdArgumentCaptor.getValue()).isEqualTo(beerId);
    }

    @Test
    void testDeleteBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
        given(beerService.deleteBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(
                    delete(BEER_PATH_ID, UUID.randomUUID())
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        verify(beerService, times(1)).deleteBeerById(any(UUID.class));
    }

    @Test
    void testDeleteBeerByIdExistsShouldDoNothing() throws Exception {
        var beerId = UUID.randomUUID();
        var beer = createDefaultBeer(beerId);
        given(beerService.deleteBeerById(beerId))
                .willReturn(Optional.of(beer));
        mockMvc.perform(
                    delete(BEER_PATH_ID, beerId)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        verify(beerService, times(1)).deleteBeerById(beerId);
    }

    @Test
    void testGetBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(
                get(BEER_PATH_WITH_SLASH + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
        verify(beerService, times(1)).getBeerById(any(UUID.class));
    }

    @Test
    void testGetBeerByIdExistsShouldReturnIt() throws Exception {
        var beerId = UUID.randomUUID();
        var beer = createDefaultBeer(beerId);
        given(beerService.getBeerById(beerId))
                .willReturn(Optional.of(beer));
        mockMvc.perform(
                    get(BEER_PATH_WITH_SLASH + beerId)
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
