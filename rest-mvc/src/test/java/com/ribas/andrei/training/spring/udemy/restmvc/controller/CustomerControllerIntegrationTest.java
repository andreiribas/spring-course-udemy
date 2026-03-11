package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.domain.repository.CustomerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.RestMvcSpringBootApplication;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateCustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testGetCustomerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus(){
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.getCustomerById(id));
        assertEquals("Customer with id %s not found".formatted(id), exception.getMessage());
    }

    @Test
    void testGetCustomerByIdExistsShouldReturnIt() {
        var id = fixture.listCustomers().getFirst().getId();

        var response = fixture.getCustomerById(id);
        var customer = response.getBody();
        assertNotNull(customer);
        assertEquals(id, customer.getId());
    }

    @Rollback
    @Transactional
    @Test
    void testCreateNewCustomerShouldWork() {
        var newCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("New Test Customer")
                .build();
        var createdCustomerResponse = fixture.createCustomer(newCustomerDTO);
        assertNotNull(createdCustomerResponse.getHeaders().getLocation());
        var idFromLocation = UUID.fromString(createdCustomerResponse.getHeaders().getLocation().getPath().split("/")[1]);
        var createdCustomer = createdCustomerResponse.getBody();
        assertNotNull(createdCustomer);
        assertEquals(idFromLocation, createdCustomer.getId());
        assertEquals(newCustomerDTO.getName(), createdCustomer.getName());
        assertNotNull(createdCustomer.getCreatedAt());
        assertNotNull(createdCustomer.getUpdatedAt());
        assertEquals(createdCustomer.getCreatedAt(), createdCustomer.getUpdatedAt());

        assertTrue(customerRepository.existsById(idFromLocation));
    }

    @Test
    void testUpdateCustomerByIdWhenItDoesNotExistShouldReturnNotFound() {
        var updatedCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Updated Customer")
                .build();
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.updateCustomerById(id, updatedCustomerDTO));
        assertEquals("Customer with id %s not found".formatted(id), exception.getMessage());
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomerByIdWhenItExistsShouldReturnOK() {
        var updatedCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Updated Customer")
                .build();
        var id = fixture.listCustomers().getFirst().getId();
        var response = fixture.updateCustomerById(id, updatedCustomerDTO);
        assertEquals(204, response.getStatusCode().value());

        var updatedCustomer = customerRepository.findById(id).orElseThrow();
        assertNotNull(updatedCustomer);
        assertEquals(updatedCustomerDTO.getName(), updatedCustomer.getName());
        assertNotNull(updatedCustomer.getCreatedAt());
        assertNotNull(updatedCustomer.getUpdatedAt());
        assertTrue(updatedCustomer.getCreatedAt().isBefore(updatedCustomer.getUpdatedAt()));
    }

    @Test
    void testPatchCustomerByIdWhenItDoesNotExistShouldReturnNotFound() {
        var updatedCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Patched Customer")
                .build();
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.patchCustomerById(id, updatedCustomerDTO));
        assertEquals("Customer with id %s not found".formatted(id), exception.getMessage());
    }

    @Rollback
    @Transactional
    @Test
    void testPatchCustomerByIdWhenItExistsShouldReturnOK() {
        var updatedCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name("Patched Customer")
                .build();
        var id = fixture.listCustomers().getFirst().getId();
        var response = fixture.patchCustomerById(id, updatedCustomerDTO);
        assertEquals(204, response.getStatusCode().value());

        var updatedCustomer = customerRepository.findById(id).orElseThrow();
        assertNotNull(updatedCustomer);
        assertEquals(updatedCustomerDTO.getName(), updatedCustomer.getName());
        assertNotNull(updatedCustomer.getCreatedAt());
        assertNotNull(updatedCustomer.getUpdatedAt());
        assertTrue(updatedCustomer.getCreatedAt().isBefore(updatedCustomer.getUpdatedAt()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchCustomerByIdWhenItExistsAndNameIsNullShouldDoNothingAndReturnOK() {
        var updatedCustomerDTO = CreateOrUpdateCustomerDTO.builder()
                .name(null)
                .build();
        var id = fixture.listCustomers().getFirst().getId();
        var response = fixture.patchCustomerById(id, updatedCustomerDTO);
        assertEquals(204, response.getStatusCode().value());

        var updatedCustomer = customerRepository.findById(id).orElseThrow();
        assertNotNull(updatedCustomer);
        assertNotEquals(updatedCustomerDTO.getName(), updatedCustomer.getName());
        assertNotNull(updatedCustomer.getCreatedAt());
        assertNotNull(updatedCustomer.getUpdatedAt());
        assertTrue(updatedCustomer.getCreatedAt().isBefore(updatedCustomer.getUpdatedAt()));
    }

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

}
