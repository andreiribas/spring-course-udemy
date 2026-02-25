package com.ribas.andrei.training.spring.udemy.restmvc.controller;

import com.ribas.andrei.training.spring.udemy.restmvc.controller.service.CustomerViewService;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.CustomerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.ribas.andrei.training.spring.udemy.restmvc.service.CustomerServiceImpl.CUSTOMER_NOT_FOUND_MESSAGE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_PATH = "/api/v1/customers";
    public static final String CUSTOMER_PATH_WITH_SLASH = CUSTOMER_PATH + "/";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerViewService customerViewService;

    private Supplier<NotFoundException> createThrowCustomerNotFoundExceptionSupplier(UUID customerId) {
        return () -> new NotFoundException(CUSTOMER_NOT_FOUND_MESSAGE.formatted(customerId));
    }

    @PostMapping(CUSTOMER_PATH)
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {

        CustomerDTO savedCustomer = customerViewService.createCustomer(customer);

        String basePath = ServletUriComponentsBuilder.fromCurrentRequest().build().getPath();
        URI location = URI.create(basePath + "/" + savedCustomer.getId());

//        var httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(location));
//        return new ResponseEntity<>(customer, httpHeaders, HttpStatus.CREATED);

        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<?> updateCustomerById(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        customerViewService.updateCustomerById(customerId, customer).orElseThrow(createThrowCustomerNotFoundExceptionSupplier(customerId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<?> deleteCustomerById(@PathVariable UUID customerId) {
        customerViewService.deleteCustomerById(customerId).orElseThrow(createThrowCustomerNotFoundExceptionSupplier(customerId));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity<?> patchCustomerById(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        customerViewService.patchCustomerById(customerId, customer).orElseThrow(createThrowCustomerNotFoundExceptionSupplier(customerId));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = CUSTOMER_PATH, method = RequestMethod.GET)
    public List<CustomerDTO> listCustomer() {
        return customerViewService.listCustomers();
    }

    @RequestMapping(value = CUSTOMER_PATH_ID, method = RequestMethod.GET)
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID customerId) {
        log.debug("Get customer by id: {} - in controller.", customerId);
        var customer = customerViewService.getCustomerById(customerId).orElseThrow(createThrowCustomerNotFoundExceptionSupplier(customerId));
        return ResponseEntity.ok(customer);
    }
}
