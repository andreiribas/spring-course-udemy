package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.domain.repository.BeerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.RestMvcSpringBootApplication;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateBeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import com.ribas.andrei.training.spring.udemy.restmvc.mapper.BeerDTOMapper;
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
        var id = fixture.listBeers().getFirst().getId();
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

}
