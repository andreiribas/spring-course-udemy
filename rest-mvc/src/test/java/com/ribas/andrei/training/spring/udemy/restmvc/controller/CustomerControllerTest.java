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
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name(null)
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.createCustomer(any(CreateOrUpdateCustomerDTO.class))).willReturn(new CustomerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(CUSTOMER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).createCustomer(any(CreateOrUpdateCustomerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotNull\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewCustomerWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("")
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.createCustomer(any(CreateOrUpdateCustomerDTO.class))).willReturn(new CustomerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(CUSTOMER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).createCustomer(any(CreateOrUpdateCustomerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewCustomerWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("  ")
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.createCustomer(any(CreateOrUpdateCustomerDTO.class))).willReturn(new CustomerDTO(id));
        var mvcResult = mockMvc.perform(
                        post(CUSTOMER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).createCustomer(any(CreateOrUpdateCustomerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateNewCustomerShouldReturnOKOKHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("New Customer")
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.createCustomer(any(CreateOrUpdateCustomerDTO.class))).willReturn(new CustomerDTO(id));
        var mvcResult = mockMvc.perform(
                    post(CUSTOMER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
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
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Customer")
                .build();
        given(customerViewService.updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.empty());
        mockMvc.perform(
                    put(CUSTOMER_PATH_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isNotFound());
        verify(customerViewService, times(1)).updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));
    }

    @Test
    void testUpdateCustomerByIdWhenNameIsNullShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name(null)
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.of(new CustomerDTO(id)));
        var mvcResult = mockMvc.perform(
                        put(CUSTOMER_PATH_ID, UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"},{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotNull\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateCustomerByIdWhenNameIsEmptyShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("")
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.of(new CustomerDTO(id)));
        var mvcResult = mockMvc.perform(
                        put(CUSTOMER_PATH_ID, UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateCustomerByIdWhenNameIsComposedOfSpacesShouldReturnBadRequestHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("  ")
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.of(new CustomerDTO(id)));
        var mvcResult = mockMvc.perform(
                        put(CUSTOMER_PATH_ID, UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(customerViewService, times(0)).updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));

        assertEquals("[{\"objectName\":\"createOrUpdateCustomerDTO\",\"fieldName\":\"name\",\"code\":\"NotBlank\"}]", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testUpdateCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Customer")
                .build();

        var id = UUID.randomUUID();
        given(customerViewService.updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.of(new CustomerDTO(id)));
        mockMvc.perform(
                    put(CUSTOMER_PATH_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createOrUpdateCustomerDTO))
                )
                .andExpect(status().isNoContent())
                .andReturn();
        verify(customerViewService, times(1)).updateCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));
    }

    @Test
    void testPatchCustomerByIdWhenItDoesNotExistShouldReturnNotFoundHttpStatusCode() throws Exception {
        var createOrUpdateCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Customer2")
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
    void testPatchCustomerByIdWhenItExistsShouldReturnNoContentHttpStatusCode() throws Exception {

        var customerId = UUID.randomUUID();

        Map<String, Object> customerFields = new HashMap<>();
        var customerName = "Patched Name";
        customerFields.put("name", customerName);

        given(customerViewService.patchCustomerById(eq(customerId), any(CreateOrUpdateCustomerDTO.class))).willReturn(Optional.of(new CustomerDTO(customerId)));
        mockMvc.perform(
                    patch(CUSTOMER_PATH_ID, customerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(customerFields))
                )
                .andExpect(status().isNoContent());
        verify(customerViewService, times(1)).patchCustomerById(any(UUID.class), any(CreateOrUpdateCustomerDTO.class));
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
    void testGetCustomerByIdExistsShouldOKHttpStatusCodeAndTheObject() throws Exception {
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
                .name("Test")
                .build();
    }

}
