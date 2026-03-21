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
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setName(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotNull\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setName("");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setName("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenStyleIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setStyle(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotNull\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenStyleIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setStyle("");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotBlank\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenStyleIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setStyle("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotBlank\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenQuantityIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setQuantity(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"quantity\",\"code\":\"NotNull\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenQuantityIsNegativeShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setQuantity(-1);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"quantity\",\"code\":\"PositiveOrZero\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenUpcIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setUpc(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotNull\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenUpcIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setUpc("");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotBlank\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenUpcIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setUpc("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotBlank\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenPriceIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setQuantity(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"quantity\",\"code\":\"NotNull\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenPriceIsNegativeShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setPrice(new BigDecimal(-1));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenPriceIsZeroShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setPrice(new BigDecimal(0));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testCreateNewBeerWhenPriceIsZeroShouldReturnBadRequestHttpStatusCodeV2() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setPrice(new BigDecimal("0.0"));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    private String testCreateNewBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(CreateOrUpdateBeerDTO createBeerDTO) throws Exception {
        var id = UUID.randomUUID();
        var mvcResult = mockMvc.perform(
                        post(BEER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createBeerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).createBeer(any(CreateOrUpdateBeerDTO.class));
        return mvcResult.getResponse().getContentAsString();
    }

    @Test
    void testCreateNewBeerShouldReturnOKHttpStatusCode() throws Exception {
        var createBeer = this.createDefaultBeer();
        testCreateNewBeerShouldReturnOKHttpStatusCodeInternal(createBeer);
    }

    @Test
    void testCreateNewBeerShouldReturnOKHttpStatusCodeV2() throws Exception {
        var createBeer = this.createDefaultBeer();
        createBeer.setQuantity(0);
        testCreateNewBeerShouldReturnOKHttpStatusCodeInternal(createBeer);
    }

    private void testCreateNewBeerShouldReturnOKHttpStatusCodeInternal(CreateOrUpdateBeerDTO createBeer) throws Exception {
        var id = UUID.randomUUID();
        given(beerViewService.createBeer(any(CreateOrUpdateBeerDTO.class))).willReturn(new BeerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(BEER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createBeer))
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
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setName(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotNull\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setName("");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setName("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenStyleIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setStyle(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotNull\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenStyleIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setStyle("");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotBlank\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenStyleIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setStyle("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NotBlank\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenQuantityIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setQuantity(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"quantity\",\"code\":\"NotNull\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenQuantityIsNegativeShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setQuantity(-1);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"quantity\",\"code\":\"PositiveOrZero\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenUpcIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setUpc(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotNull\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenUpcIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setUpc("");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotBlank\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenUpcIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setUpc("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NotBlank\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenPriceIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setQuantity(null);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"quantity\",\"code\":\"NotNull\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenPriceIsNegativeShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setPrice(new BigDecimal(-1));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenPriceIsZeroShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setPrice(new BigDecimal(0));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    @Test
    void testUpdateBeerWhenPriceIsZeroShouldReturnBadRequestHttpStatusCodeV2() throws Exception {
        var createBeerDTO = this.createDefaultBeer();
        createBeerDTO.setPrice(new BigDecimal("0.0"));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createBeerDTO));
    }

    private String testUpdateBeerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(CreateOrUpdateBeerDTO updateBeer) throws Exception {
        var id = UUID.randomUUID();
        var mvcResult = mockMvc.perform(
                        put(BEER_PATH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateBeer))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).updateBeerById(any(UUID.class), any(CreateOrUpdateBeerDTO.class));
        return mvcResult.getResponse().getContentAsString();
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
        testUpdateBeerByIdShouldReturnNoContentHttpStatusCode(createOrUpdateBeerDTO);
    }

    @Test
    void testUpdateBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeV2() throws Exception {
        var createOrUpdateBeerDTO = CreateOrUpdateBeerDTO.builder()
                .name("Maes")
                .style("Pilsen")
                .quantity(0)
                .upc("7581004782")
                .price(new BigDecimal("1.6"))
                .build();
        testUpdateBeerByIdShouldReturnNoContentHttpStatusCode(createOrUpdateBeerDTO);
    }

    private void testUpdateBeerByIdShouldReturnNoContentHttpStatusCode(CreateOrUpdateBeerDTO createOrUpdateBeerDTO) throws Exception {
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
    void testPatchBeerByIdWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("name", "");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NullOrNotBlank\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("name", "  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"name\",\"code\":\"NullOrNotBlank\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenStyleIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("style", "");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NullOrNotBlank\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenStyleIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("style", "  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"style\",\"code\":\"NullOrNotBlank\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenQuantityIsNegativeShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("quantity", -1);
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"quantity\",\"code\":\"PositiveOrZero\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenUpcIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("upc", "");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NullOrNotBlank\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenUpcIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("upc", "  ");
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"upc\",\"code\":\"NullOrNotBlank\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenPriceIsNegativeShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("price", new BigDecimal(-1));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenPriceIsZeroShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("price", new BigDecimal(0));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchBeerByIdWhenPriceIsZeroShouldReturnBadRequestHttpStatusCodeV2() throws Exception {
        Map<String, Object> patchedFields = Map.of("price", new BigDecimal("0.0"));
        assertEquals("[{\"objectName\":\"createOrUpdateBeerDTO\",\"fieldName\":\"price\",\"code\":\"Positive\"}]", testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    private String testPatchBeerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(Map<String, Object> patchedFields) throws Exception {
        var beerId = UUID.randomUUID();
        var mvcResult = mockMvc.perform(
                        patch(BEER_PATH_ID, beerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchedFields))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(beerViewService, times(0)).patchBeerById(eq(beerId), any(CreateOrUpdateBeerDTO.class));
        return mvcResult.getResponse().getContentAsString();
    }

    @Test
    void testPatchBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCode() throws Exception {
        Map<String, Object> beerFields = new HashMap<>();
        var beerName = "Maes";
        var beerStyle = "Pilsen";
        beerFields.put("name", beerName);
        beerFields.put("style", beerStyle);
        beerFields.put("quantity", 65);
        testPatchBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeInternal(beerFields);
    }

    @Test
    void testPatchBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeV2() throws Exception {
        Map<String, Object> beerFields = new HashMap<>();
        var beerName = "Maes";
        var beerStyle = "Pilsen";
        beerFields.put("name", beerName);
        beerFields.put("style", beerStyle);
        beerFields.put("quantity", 0);
        testPatchBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeInternal(beerFields);
    }

    private void testPatchBeerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeInternal(Map<String, Object> patchedFields) throws Exception {
        var beerId = UUID.randomUUID();

        given(beerViewService.patchBeerById(eq(beerId), any(CreateOrUpdateBeerDTO.class))).willReturn(Optional.of(new BeerDTO(beerId)));
        mockMvc.perform(
                        patch(BEER_PATH_ID, beerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchedFields))
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

    private CreateOrUpdateBeerDTO createDefaultBeer() {
        return CreateOrUpdateBeerDTO.builder()
                .name("Jupiler")
                .style("Pilsen")
                .quantity(48)
                .upc("7561238480")
                .price(new BigDecimal("3.5"))
                .build();
    }

}
