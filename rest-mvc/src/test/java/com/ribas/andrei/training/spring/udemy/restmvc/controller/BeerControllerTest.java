package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.controller.service.BeerViewService;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateBeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import org.junit.jupiter.api.Test;
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
    private BeerViewService beerViewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListBeersWhenThereAreNoBeersShouldReturnOKHttpStatusCodeAndEmptyList() throws Exception {
        given(beerViewService.listBeers())
                .willReturn(Collections.emptyList());
        mockMvc.perform(
                    get(BEER_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));
        verify(beerViewService, times(1)).listBeers();
    }

    @Test
    void testListBeersWhenThereAreBeersShouldReturnOKHttpStatusCodeAndListWithValues() throws Exception {
        var beers = List.of(createDefaultBeer(UUID.randomUUID()),
                createDefaultBeer(UUID.randomUUID()));
        given(beerViewService.listBeers())
                .willReturn(beers);
        mockMvc.perform(
                    get(BEER_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));

        verify(beerViewService, times(1)).listBeers();
    }

    @Test
    void testCreateNewBeerWhenNameIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name(null)
                .style("Pilsen")
                .quantity(48)
                .upc("7561238480")
                .price(new BigDecimal("3.5"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.createBeer(any(CreateOrUpdateBeerDTO.class))).willReturn(new BeerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(BEER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).createBeer(any(CreateOrUpdateBeerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotNull\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewBeerWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("     ")
                .style("Pilsen")
                .quantity(48)
                .upc("7561238480")
                .price(new BigDecimal("3.5"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.createBeer(any(CreateOrUpdateBeerDTO.class))).willReturn(new BeerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(BEER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).createBeer(any(CreateOrUpdateBeerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewBeerWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("")
                .style("Pilsen")
                .quantity(48)
                .upc("7561238480")
                .price(new BigDecimal("3.5"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.createBeer(any(CreateOrUpdateBeerDTO.class))).willReturn(new BeerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(BEER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).createBeer(any(CreateOrUpdateBeerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewBeerShouldReturnOKHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Jupiler")
                .style("Pilsen")
                .quantity(48)
                .upc("7561238480")
                .price(new BigDecimal("3.5"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.createBeer(any(CreateOrUpdateBeerDTO.class))).willReturn(new BeerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(BEER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isCreated())
                .andReturn();
        verify(beerViewService, times(1)).createBeer(any(CreateOrUpdateBeerDTO.class));

        // assert Location header contains the captured id
        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals(BEER_PATH_WITH_SLASH + id, location);
    }

    @Test
    void testUpdateBeerByIdWhenItDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();
        given(beerViewService.updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class))).willReturn(Optional.empty());
        mockMvc.perform(
                    put(BEER_PATH_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isNotFound());
        verify(beerViewService, times(1)).updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class));
    }

    @Test
    void testUpdateBeerWhenNameIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name(null)
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class))).willReturn(Optional.of(new BeerDTO(id)));
        var mvcResult = mockMvc.perform(
                        put(BEER_PATH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class));
    }

    @Test
    void testUpdateBeerWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class))).willReturn(Optional.of(new BeerDTO(id)));
        var mvcResult = mockMvc.perform(
                        put(BEER_PATH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateBeerWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("  ")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class))).willReturn(Optional.of(new BeerDTO(id)));
        var mvcResult = mockMvc.perform(
                        put(BEER_PATH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();

        var id = UUID.randomUUID();
        given(beerViewService.updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class))).willReturn(Optional.of(new BeerDTO(id)));
        mockMvc.perform(
                    put(BEER_PATH_ID, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isNoContent());
        verify(beerViewService, times(1)).updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class));
    }

    @Test
    void testPatchBeerByIdWhenItDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(50)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();
        given(beerViewService.patchBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class))).willThrow(new NotFoundException("Beer not found"));
        mockMvc.perform(
                    patch(BEER_PATH_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateBeerDTO))
                )
                .andExpect(status().isNotFound());
        verify(beerViewService, times(1)).patchBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class));
    }

    @Test
    void testPatchBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCode() throws Exception {

        var beerId = UUID.randomUUID();

        Map<String, Object> beerFields = new HashMap<>();
        var beerName = "Maes";
        var beerStyle = "Pilsen";
        beerFields.put("name", beerName);
        beerFields.put("style", beerStyle);

        given(beerViewService.patchBeerById(eq(beerId), any(CreateOrUpdateBeerDTO.class))).willReturn(Optional.of(new BeerDTO(beerId)));
        mockMvc.perform(
                    patch(BEER_PATH_ID, beerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beerFields))
                )
                .andExpect(status().isNoContent());
        verify(beerViewService, times(1)).patchBeerById(eq(beerId), any(CreateOrUpdateBeerDTO.class));
    }

    @Test
    void testDeleteBeerByIdWhenIdDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        given(beerViewService.deleteBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(
                    delete(BEER_PATH_ID, UUID.randomUUID())
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        verify(beerViewService, times(1)).deleteBeerById(any(UUID.class));
    }

    @Test
    void testDeleteBeerByIdExistsShouldReturnNoContentHttpStatusCode() throws Exception {
        var beerId = UUID.randomUUID();
        var beer = createDefaultBeer(beerId);
        given(beerViewService.deleteBeerById(beerId))
                .willReturn(Optional.of(beer));
        mockMvc.perform(
                    delete(BEER_PATH_ID, beerId)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        verify(beerViewService, times(1)).deleteBeerById(beerId);
    }

    @Test
    void testGetBeerByIdWhenIdDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        given(beerViewService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(
                get(BEER_PATH_WITH_SLASH + UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());
        verify(beerViewService, times(1)).getBeerById(any(UUID.class));
    }

    @Test
    void testGetBeerByIdExistsShouldReturnOKHttpStatusCodeAndTheObject() throws Exception {
        var beerId = UUID.randomUUID();
        var beer = createDefaultBeer(beerId);
        given(beerViewService.getBeerById(beerId))
                .willReturn(Optional.of(beer));
        mockMvc.perform(
                    get(BEER_PATH_WITH_SLASH + beerId)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(beerId.toString())))
            .andExpect(jsonPath("$.name", is(beer.getName())));
        verify(beerViewService, times(1)).getBeerById(beerId);
    }

    private BeerDTO createDefaultBeer(UUID beerId) {
        var now = LocalDateTime.now();
        return BeerDTO.builder()
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
