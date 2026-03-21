package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.controller.service.CustomerViewService;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateCustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.*;

import static com.ribas.andrei.training.spring.udemy.restmvc.controller.CustomerController.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerViewService customerViewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListCustomersWhenThereAreNoCustomersShouldReturnOKHttpStatusCodeAndEmptyList() throws Exception {
        given(customerViewService.listCustomers())
                .willReturn(Collections.emptyList());
        mockMvc.perform(
                    get(CUSTOMER_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));
        verify(customerViewService, times(1)).listCustomers();
    }

    @Test
    void testListCustomersWhenThereAreCustomersShouldReturnOKHttpStatusCodeAndListWithValues() throws Exception {
        var customers = List.of(createDefaultCustomer(UUID.randomUUID()),
                createDefaultCustomer(UUID.randomUUID()));
        given(customerViewService.listCustomers())
                .willReturn(customers);
        mockMvc.perform(
                    get(CUSTOMER_PATH)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));

        verify(customerViewService, times(1)).listCustomers();
    }

    @Test
    void testCreateNewCustomerWhenNameIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createCustomerDTO = this.createDefaultCustomer();
        createCustomerDTO.setName(null);
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotNull\"}]", testCreateNewCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createCustomerDTO));
    }

    @Test
    void testCreateNewCustomerWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createCustomerDTO = this.createDefaultCustomer();
        createCustomerDTO.setName("");
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testCreateNewCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createCustomerDTO));
    }

    @Test
    void testCreateNewCustomerWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createCustomerDTO = this.createDefaultCustomer();
        createCustomerDTO.setName("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testCreateNewCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createCustomerDTO));
    }

    private String testCreateNewCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(CreateOrUpdateCustomerDTO createCustomerDTO) throws Exception {
        var mvcResult = mockMvc.perform(
                        post(CUSTOMER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).createCustomer(any(CreateOrUpdateCustomerDTO.class));
        return mvcResult.getResponse().getContentAsString();
    }

    @Test
    void testCreateNewCustomerShouldReturnOKHttpStatusCode() throws Exception {
        var createCustomer = this.createDefaultCustomer();
        var id = UUID.randomUUID();
        given(customerViewService.createCustomer(any(CreateOrUpdateCustomerDTO.class))).willReturn(new CustomerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(CUSTOMER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCustomer))
                )
                .andExpect(status().isCreated())
                .andReturn();
        verify(customerViewService, times(1)).createCustomer(any(CreateOrUpdateCustomerDTO.class));

        // assert Location header contains the captured id
        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals(CUSTOMER_PATH_WITH_SLASH + id, location);
    }

    @Test
    void testUpdateCustomerByIdWhenItDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        var id = UUID.randomUUID();
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Maes")
                .build();
        given(customerViewService.updateCustomerById(eq(id), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.empty());
        mockMvc.perform(
                    put(CUSTOMER_PATH_ID, id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isNotFound());
        verify(customerViewService, times(1)).updateCustomerById(eq(id), any(CreateOrUpdateCustomerDTO.class));
    }

    @Test
    void testUpdateCustomerWhenNameIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createCustomerDTO = this.createDefaultCustomer();
        createCustomerDTO.setName(null);
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotNull\"}]", testUpdateCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createCustomerDTO));
    }

    @Test
    void testUpdateCustomerWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createCustomerDTO = this.createDefaultCustomer();
        createCustomerDTO.setName("");
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testUpdateCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createCustomerDTO));
    }

    @Test
    void testUpdateCustomerWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createCustomerDTO = this.createDefaultCustomer();
        createCustomerDTO.setName("  ");
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", testUpdateCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(createCustomerDTO));
    }

    private String testUpdateCustomerWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(CreateOrUpdateCustomerDTO updateCustomer) throws Exception {
        var id = UUID.randomUUID();
        var mvcResult = mockMvc.perform(
                        put(CUSTOMER_PATH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateCustomer))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));
        return mvcResult.getResponse().getContentAsString();
    }

    @Test
    void testUpdateCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Maes")
                .build();
        testUpdateCustomerByIdShouldReturnNoContentHttpStatusCode(createOrUpdateCustomerDTO);
    }

    @Test
    void testUpdateCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeV2() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Maes")
                .build();
        testUpdateCustomerByIdShouldReturnNoContentHttpStatusCode(createOrUpdateCustomerDTO);
    }

    private void testUpdateCustomerByIdShouldReturnNoContentHttpStatusCode(CreateOrUpdateCustomerDTO createOrUpdateCustomerDTO) throws Exception {
        var id = UUID.randomUUID();
        given(customerViewService.updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.of(new CustomerDTO(id)));
        mockMvc.perform(
                        put(CUSTOMER_PATH_ID, id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isNoContent());
        verify(customerViewService, times(1)).updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));
    }

    @Test
    void testPatchCustomerByIdWhenItDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Maes")
                .build();
        given(customerViewService.patchCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class))).willThrow(new NotFoundException("Customer not found"));
        mockMvc.perform(
                        patch(CUSTOMER_PATH_ID, UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isNotFound());
        verify(customerViewService, times(1)).patchCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));
    }

    @Test
    void testPatchCustomerByIdWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("name", "");
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NullOrNotBlank\"}]", testPatchCustomerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    @Test
    void testPatchCustomerByIdWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        Map<String, Object> patchedFields = Map.of("name", "  ");
        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NullOrNotBlank\"}]", testPatchCustomerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(patchedFields));
    }

    private String testPatchCustomerByIdWhenThereIsAValidationErrorShouldReturnBadRequestHttpStatusCode(Map<String, Object> patchedFields) throws Exception {
        var customerId = UUID.randomUUID();
        var mvcResult = mockMvc.perform(
                        patch(CUSTOMER_PATH_ID, customerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchedFields))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).patchCustomerById(eq(customerId), any(CreateOrUpdateCustomerDTO.class));
        return mvcResult.getResponse().getContentAsString();
    }

    @Test
    void testPatchCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCode() throws Exception {
        Map<String, Object> customerFields = new HashMap<>();
        var customerName = "Maes";
        var customerStyle = "Pilsen";
        customerFields.put("name", customerName);
        customerFields.put("style", customerStyle);
        customerFields.put("quantity", 65);
        testPatchCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeInternal(customerFields);
    }

    @Test
    void testPatchCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeV2() throws Exception {
        Map<String, Object> customerFields = new HashMap<>();
        var customerName = "Maes";
        var customerStyle = "Pilsen";
        customerFields.put("name", customerName);
        customerFields.put("style", customerStyle);
        customerFields.put("quantity", 0);
        testPatchCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeInternal(customerFields);
    }

    private void testPatchCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCodeInternal(Map<String, Object> patchedFields) throws Exception {
        var customerId = UUID.randomUUID();

        given(customerViewService.patchCustomerById(eq(customerId), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.of(new CustomerDTO(customerId)));
        mockMvc.perform(
                        patch(CUSTOMER_PATH_ID, customerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchedFields))
                )
                .andExpect(status().isNoContent());
        verify(customerViewService, times(1)).patchCustomerById(eq(customerId), any(CreateOrUpdateCustomerDTO.class));
    }

    @Test
    void testDeleteCustomerByIdWhenIdDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        given(customerViewService.deleteCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(
                        delete(CUSTOMER_PATH_ID, UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        verify(customerViewService, times(1)).deleteCustomerById(any(UUID.class));
    }

    @Test
    void testDeleteCustomerByIdExistsShouldReturnNoContentHttpStatusCode() throws Exception {
        var customerId = UUID.randomUUID();
        var customer = createDefaultCustomer(customerId);
        given(customerViewService.deleteCustomerById(customerId))
                .willReturn(Optional.of(customer));
        mockMvc.perform(
                        delete(CUSTOMER_PATH_ID, customerId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
        verify(customerViewService, times(1)).deleteCustomerById(customerId);
    }

    @Test
    void testGetCustomerByIdWhenIdDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        given(customerViewService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(
                        get(CUSTOMER_PATH_WITH_SLASH + UUID.randomUUID())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        verify(customerViewService, times(1)).getCustomerById(any(UUID.class));
    }

    @Test
    void testGetCustomerByIdExistsShouldReturnOKHttpStatusCodeAndTheObject() throws Exception {
        var customerId = UUID.randomUUID();
        var customer = createDefaultCustomer(customerId);
        given(customerViewService.getCustomerById(customerId))
                .willReturn(Optional.of(customer));
        mockMvc.perform(
                        get(CUSTOMER_PATH_WITH_SLASH + customerId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customerId.toString())))
                .andExpect(jsonPath("$.name", is(customer.getName())));
        verify(customerViewService, times(1)).getCustomerById(customerId);
    }

    private CustomerDTO createDefaultCustomer(UUID customerId) {
        var now = LocalDateTime.now();
        return CustomerDTO.builder()
                .id(customerId)
                .name("Jupiler")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private CreateOrUpdateCustomerDTO createDefaultCustomer() {
        return CreateOrUpdateCustomerDTO.builder()
                .name("Jupiler")
                .build();
    }

}
