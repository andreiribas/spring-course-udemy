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

@SpringBootTest(classes = RestMvcSpringBootApplication.class)
class BeerControllerIntegrationTest {

    @Autowired
    private BeerController fixture;

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void testListBeersWhenThereAreBeersShouldReturnListWithValues() {
        assertEquals(3, fixture.listBeers().size());
    }

    @Rollback
    @Transactional
    @Test
    void testListBeersWhenThereAreNoBeersShouldReturnEmptyList() {
        beerRepository.deleteAll();
        assertTrue(fixture.listBeers().isEmpty());
    }

    @Test
    void testGetBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() {
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.getBeerById(id));
        assertEquals("Beer with id %s not found".formatted(id), exception.getMessage());
    }

    @Test
    void testGetBeerByIdExistsShouldReturnIt() {
        var id = fixture.listBeers().getFirst().getId();

        var response = fixture.getBeerById(id);
        var customer = response.getBody();
        assertNotNull(customer);
        assertEquals(id, customer.getId());
    }

    @Test
    void testCreateNewBeerShouldWork() {
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
//
//    @Test
//    void testCreateNewBeerShouldWork() throws Exception {
//        BeerDTO beer = BeerDTO.builder()
//                .name("Jupiler")
//                .style("Pilsen")
//                .quantity(48)
//                .upc("7561238480")
//                .price(new BigDecimal("3.5")).build();
//
//        given(beerViewService.createBeer(any(BeerDTO.class))).willAnswer(a -> {
//            var newBeer = a.getArgument(0, BeerDTO.class);
//            newBeer.setId(UUID.randomUUID());
//            return newBeer;
//        });
//        var beerArgumentCaptor = ArgumentCaptor.forClass(BeerDTO.class);
//        var mvcResult = mockMvc.perform(
//                    post(BEER_PATH)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(beer))
//                )
//                .andExpect(status().isCreated())
//                .andReturn();
//        verify(beerViewService, times(1)).createBeer(beerArgumentCaptor.capture());
//
//        var capturedBeer = beerArgumentCaptor.getValue();
//        // assert Location header contains the captured id
//        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
//        assertEquals(BEER_PATH_WITH_SLASH + capturedBeer.getId(), location);
//    }
//
//    @Test
//    void testUpdateBeerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
//        BeerDTO beer = BeerDTO.builder()
//                .name("Maes")
//                .style("Pilsen")
//                .quantity(50)
//                .upc("7581004782")
//                .price(new BigDecimal("1.6")).build();
//        given(beerViewService.updateBeerById(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.empty());
//        mockMvc.perform(
//                    put(BEER_PATH_ID, UUID.randomUUID())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(beer))
//                )
//                .andExpect(status().isNotFound());
//        verify(beerViewService, times(1)).updateBeerById(any(UUID.class), any(BeerDTO.class));
//    }
//
//    @Test
//    void testUpdateBeerByIdWhenItExistsShouldReturnOK() throws Exception {
//        BeerDTO beer = BeerDTO.builder()
//                .name("Maes")
//                .style("Pilsen")
//                .quantity(50)
//                .upc("7581004782")
//                .price(new BigDecimal("1.6")).build();
//        given(beerViewService.updateBeerById(any(UUID.class), any(BeerDTO.class))).willAnswer(a -> Optional.of(a.getArgument(1, BeerDTO.class)));
//        mockMvc.perform(
//                    put(BEER_PATH_ID, UUID.randomUUID())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(beer))
//                )
//                .andExpect(status().isNoContent());
//        verify(beerViewService, times(1)).updateBeerById(any(UUID.class), any(BeerDTO.class));
//    }
//
//    @Test
//    void testPatchBeerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
//        BeerDTO beer = BeerDTO.builder()
//                .name("Maes")
//                .style("Pilsen")
//                .quantity(50)
//                .upc("7581004782")
//                .price(new BigDecimal("1.6")).build();
//        given(beerViewService.patchBeerById(any(UUID.class), any(BeerDTO.class))).willThrow(new NotFoundException("Beer not found"));
//        mockMvc.perform(
//                    patch(BEER_PATH_ID, UUID.randomUUID())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(beer))
//                )
//                .andExpect(status().isNotFound());
//        verify(beerViewService, times(1)).patchBeerById(any(UUID.class), any(BeerDTO.class));
//    }
//
//    @Test
//    void testPatchBeerByIdWhenItExistsShouldReturnOK() throws Exception {
//
//        Map<String, Object> beerFields = new HashMap<>();
//        var beerName = "Maes";
//        var beerStyle = "Pilsen";
//        beerFields.put("name", beerName);
//        beerFields.put("style", beerStyle);
//
//        var beerIdArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
//        var beerArgumentCaptor = ArgumentCaptor.forClass(BeerDTO.class);
//
//        var beerId = UUID.randomUUID();
//        given(beerViewService.patchBeerById(eq(beerId), any(BeerDTO.class))).willAnswer(a -> Optional.of(a.getArgument(1, BeerDTO.class)));
//        mockMvc.perform(
//                    patch(BEER_PATH_ID, beerId)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(beerFields))
//                )
//                .andExpect(status().isNoContent());
//        verify(beerViewService, times(1)).patchBeerById(beerIdArgumentCaptor.capture(), beerArgumentCaptor.capture());
//
//        assertThat(beerArgumentCaptor.getValue().getName()).isEqualTo(beerName);
//        assertThat(beerArgumentCaptor.getValue().getStyle()).isEqualTo(beerStyle);
//        assertThat(beerIdArgumentCaptor.getValue()).isEqualTo(beerId);
//    }
//
//    @Test
//    void testDeleteBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
//        given(beerViewService.deleteBeerById(any(UUID.class))).willReturn(Optional.empty());
//        mockMvc.perform(
//                    delete(BEER_PATH_ID, UUID.randomUUID())
//                    .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isNotFound());
//        verify(beerViewService, times(1)).deleteBeerById(any(UUID.class));
//    }
//
//    @Test
//    void testDeleteBeerByIdExistsShouldDoNothing() throws Exception {
//        var beerId = UUID.randomUUID();
//        var beer = createDefaultBeer(beerId);
//        given(beerViewService.deleteBeerById(beerId))
//                .willReturn(Optional.of(beer));
//        mockMvc.perform(
//                    delete(BEER_PATH_ID, beerId)
//                    .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isNoContent());
//        verify(beerViewService, times(1)).deleteBeerById(beerId);
//    }
//
//    @Test
//    void testGetBeerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
//        given(beerViewService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
//        mockMvc.perform(
//                get(BEER_PATH_WITH_SLASH + UUID.randomUUID())
//                .accept(MediaType.APPLICATION_JSON)
//            )
//            .andExpect(status().isNotFound());
//        verify(beerViewService, times(1)).getBeerById(any(UUID.class));
//    }
//
//    @Test
//    void testGetBeerByIdExistsShouldReturnIt() throws Exception {
//        var beerId = UUID.randomUUID();
//        var beer = createDefaultBeer(beerId);
//        given(beerViewService.getBeerById(beerId))
//                .willReturn(Optional.of(beer));
//        mockMvc.perform(
//                    get(BEER_PATH_WITH_SLASH + beerId)
//                    .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id", is(beerId.toString())))
//            .andExpect(jsonPath("$.name", is(beer.getName())));
//        verify(beerViewService, times(1)).getBeerById(beerId);
//    }
//
//    private BeerDTO createDefaultBeer(UUID beerId) {
//        var now = LocalDateTime.now();
//        return BeerDTO.builder()
//                .id(beerId)
//                .name("Test")
//                .style("Stout")
//                .quantity(147)
//                .upc("459675121340")
//                .price(new BigDecimal("2.47"))
//                .createdAt(now)
//                .updatedAt(now)
//                .build();
//    }

}
