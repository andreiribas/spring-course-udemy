package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.domain.repository.CustomerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.RestMvcSpringBootApplication;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = RestMvcSpringBootApplication.class)
class CustomerControllerIntegrationTest {

    @Autowired
    private CustomerController fixture;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testListCustomersWhenThereAreCustomersShouldReturnListWithValues() {
        assertEquals(2, fixture.listCustomers().size());
    }

    @Rollback
    @Transactional
    @Test
    void testListCustomersWhenThereAreNoCustomersShouldReturnEmptyList() {
        customerRepository.deleteAll();
        assertTrue(fixture.listCustomers().isEmpty());
    }
//
//    @Test
//    void testCreateNewCustomerShouldWork() throws Exception {
//        CustomerDTO customer = CustomerDTO.builder()
//                .name("New Test Customer")
//                .build();
//
//        given(customerViewService.createCustomer(any(CustomerDTO.class))).willAnswer(a -> {
//            var newCustomer = a.getArgument(0, CustomerDTO.class);
//            newCustomer.setId(UUID.randomUUID());
//            return newCustomer;
//        });
//        var customerArgumentCaptor = ArgumentCaptor.forClass(CustomerDTO.class);
//        var mvcResult = mockMvc.perform(
//                    post(BEER_PATH)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(customer))
//                )
//                .andExpect(status().isCreated())
//                .andReturn();
//        verify(customerViewService, times(1)).createCustomer(customerArgumentCaptor.capture());
//
//        var capturedCustomer = customerArgumentCaptor.getValue();
//        // assert Location header contains the captured id
//        String location = mvcResult.getResponse().getHeader(HttpHeaders.LOCATION);
//        assertEquals(BEER_PATH_WITH_SLASH + capturedCustomer.getId(), location);
//    }
//
//    @Test
//    void testUpdateCustomerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
//        CustomerDTO customer = CustomerDTO.builder()
//                .name("Maes")
//                .style("Pilsen")
//                .quantity(50)
//                .upc("7581004782")
//                .price(new BigDecimal("1.6")).build();
//        given(customerViewService.updateCustomerById(any(UUID.class), any(CustomerDTO.class))).willReturn(Optional.empty());
//        mockMvc.perform(
//                    put(BEER_PATH_ID, UUID.randomUUID())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(customer))
//                )
//                .andExpect(status().isNotFound());
//        verify(customerViewService, times(1)).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
//    }
//
//    @Test
//    void testUpdateCustomerByIdWhenItExistsShouldReturnOK() throws Exception {
//        CustomerDTO customer = CustomerDTO.builder()
//                .name("Maes")
//                .style("Pilsen")
//                .quantity(50)
//                .upc("7581004782")
//                .price(new BigDecimal("1.6")).build();
//        given(customerViewService.updateCustomerById(any(UUID.class), any(CustomerDTO.class))).willAnswer(a -> Optional.of(a.getArgument(1, CustomerDTO.class)));
//        mockMvc.perform(
//                    put(BEER_PATH_ID, UUID.randomUUID())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(customer))
//                )
//                .andExpect(status().isNoContent());
//        verify(customerViewService, times(1)).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
//    }
//
//    @Test
//    void testPatchCustomerByIdWhenItDoesNotExistShouldReturnNotFound() throws Exception {
//        CustomerDTO customer = CustomerDTO.builder()
//                .name("Maes")
//                .style("Pilsen")
//                .quantity(50)
//                .upc("7581004782")
//                .price(new BigDecimal("1.6")).build();
//        given(customerViewService.patchCustomerById(any(UUID.class), any(CustomerDTO.class))).willThrow(new NotFoundException("Customer not found"));
//        mockMvc.perform(
//                    patch(BEER_PATH_ID, UUID.randomUUID())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(customer))
//                )
//                .andExpect(status().isNotFound());
//        verify(customerViewService, times(1)).patchCustomerById(any(UUID.class), any(CustomerDTO.class));
//    }
//
//    @Test
//    void testPatchCustomerByIdWhenItExistsShouldReturnOK() throws Exception {
//
//        Map<String, Object> customerFields = new HashMap<>();
//        var customerName = "Maes";
//        var customerStyle = "Pilsen";
//        customerFields.put("name", customerName);
//        customerFields.put("style", customerStyle);
//
//        var customerIdArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
//        var customerArgumentCaptor = ArgumentCaptor.forClass(CustomerDTO.class);
//
//        var customerId = UUID.randomUUID();
//        given(customerViewService.patchCustomerById(eq(customerId), any(CustomerDTO.class))).willAnswer(a -> Optional.of(a.getArgument(1, CustomerDTO.class)));
//        mockMvc.perform(
//                    patch(BEER_PATH_ID, customerId)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(customerFields))
//                )
//                .andExpect(status().isNoContent());
//        verify(customerViewService, times(1)).patchCustomerById(customerIdArgumentCaptor.capture(), customerArgumentCaptor.capture());
//
//        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(customerName);
//        assertThat(customerArgumentCaptor.getValue().getStyle()).isEqualTo(customerStyle);
//        assertThat(customerIdArgumentCaptor.getValue()).isEqualTo(customerId);
//    }
//
//    @Test
//    void testDeleteCustomerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
//        given(customerViewService.deleteCustomerById(any(UUID.class))).willReturn(Optional.empty());
//        mockMvc.perform(
//                    delete(BEER_PATH_ID, UUID.randomUUID())
//                    .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isNotFound());
//        verify(customerViewService, times(1)).deleteCustomerById(any(UUID.class));
//    }
//
//    @Test
//    void testDeleteCustomerByIdExistsShouldDoNothing() throws Exception {
//        var customerId = UUID.randomUUID();
//        var customer = createDefaultCustomer(customerId);
//        given(customerViewService.deleteCustomerById(customerId))
//                .willReturn(Optional.of(customer));
//        mockMvc.perform(
//                    delete(BEER_PATH_ID, customerId)
//                    .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isNoContent());
//        verify(customerViewService, times(1)).deleteCustomerById(customerId);
//    }
//
//    @Test
//    void testGetCustomerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
//        given(customerViewService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
//        mockMvc.perform(
//                get(BEER_PATH_WITH_SLASH + UUID.randomUUID())
//                .accept(MediaType.APPLICATION_JSON)
//            )
//            .andExpect(status().isNotFound());
//        verify(customerViewService, times(1)).getCustomerById(any(UUID.class));
//    }
//
//    @Test
//    void testGetCustomerByIdExistsShouldReturnIt() throws Exception {
//        var customerId = UUID.randomUUID();
//        var customer = createDefaultCustomer(customerId);
//        given(customerViewService.getCustomerById(customerId))
//                .willReturn(Optional.of(customer));
//        mockMvc.perform(
//                    get(BEER_PATH_WITH_SLASH + customerId)
//                    .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//            .andExpect(jsonPath("$.id", is(customerId.toString())))
//            .andExpect(jsonPath("$.name", is(customer.getName())));
//        verify(customerViewService, times(1)).getCustomerById(customerId);
//    }
//
//    private CustomerDTO createDefaultCustomer(UUID customerId) {
//        var now = LocalDateTime.now();
//        return CustomerDTO.builder()
//                .id(customerId)
//                .name("Test")
//                .build();
//    }

}
