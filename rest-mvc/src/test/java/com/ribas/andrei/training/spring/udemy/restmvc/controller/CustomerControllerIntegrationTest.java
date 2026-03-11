package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.domain.repository.CustomerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.RestMvcSpringBootApplication;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CreateOrUpdateCustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
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
        var response = fixture.listCustomers();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Rollback
    @Transactional
    @Test
    void testListCustomersWhenThereAreNoCustomersShouldReturnEmptyList() {
        customerRepository.deleteAll();
        var response = fixture.listCustomers();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetCustomerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus(){
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.getCustomerById(id));
        assertEquals("Customer with id %s not found".formatted(id), exception.getMessage());
    }

    @Test
    void testGetCustomerByIdExistsShouldReturnIt() {
        var id = getFirstCustomerFromList().getId();

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
        var id = getFirstCustomerFromList().getId();
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
        var id = getFirstCustomerFromList().getId();
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
        var id = getFirstCustomerFromList().getId();
        var response = fixture.patchCustomerById(id, updatedCustomerDTO);
        assertEquals(204, response.getStatusCode().value());

        var updatedCustomer = customerRepository.findById(id).orElseThrow();
        assertNotNull(updatedCustomer);
        assertNotEquals(updatedCustomerDTO.getName(), updatedCustomer.getName());
        assertNotNull(updatedCustomer.getCreatedAt());
        assertNotNull(updatedCustomer.getUpdatedAt());
        assertTrue(updatedCustomer.getCreatedAt().isBefore(updatedCustomer.getUpdatedAt()));
    }

    @Test
    void testDeleteCustomerByIdWhenIdDoesNotExistShouldReturnNoFoundStatus() throws Exception {
        var id = UUID.randomUUID();
        var exception = assertThrows(NotFoundException.class, () -> fixture.deleteCustomerById(id));
        assertEquals("Customer with id %s not found".formatted(id), exception.getMessage());
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerByIdExistsShouldDoNothing() throws Exception {
        var customerId = getFirstCustomerFromList().getId();
        fixture.deleteCustomerById(customerId);
        var exception = assertThrows(NotFoundException.class, () -> fixture.getCustomerById(customerId));
        assertEquals("Customer with id %s not found".formatted(customerId), exception.getMessage());
    }

    private CustomerDTO getFirstCustomerFromList() {
        return fixture.listCustomers().getBody().getFirst();
    }

}
