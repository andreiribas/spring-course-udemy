package com.ribas.andrei.training.spring.udemy.domain.repository;

import com.ribas.andrei.training.spring.udemy.domain.model.Customer;
import com.ribas.andrei.training.spring.udemy.domain.test.DomainDataGenerator;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static com.ribas.andrei.training.spring.udemy.domain.test.BeanValidationUtils.checkValidationExceptionIsDueToBlankStringField;
import static com.ribas.andrei.training.spring.udemy.domain.test.BeanValidationUtils.checkValidationExceptionIsDueToFieldStringSize;
import static com.ribas.andrei.training.spring.udemy.domain.test.BeanValidationUtils.checkValidationExceptionIsDueToNullField;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTestImpl
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    @Rollback
    @Test
    void testSaveCustomer() {
        var customer = customerRepository.save(DomainDataGenerator.generateDefaultCustomer());
        customerRepository.flush();
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

    @Transactional
    @Rollback
    @Test
    void testSaveCustomerV2() {
        var customer = customerRepository.save(DomainDataGenerator.generateDefaultCustomerBuilder()
                .name(RandomStringUtils.secure().nextAlphanumeric(50))
                .build());
        customerRepository.flush();
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }

    // name field validation errors

    @Test
    void testSaveCustomerWhenNameIsNullShouldThrowException() {
        customerRepository.save(DomainDataGenerator.generateDefaultCustomerBuilder().name(null).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> customerRepository.flush());
        checkValidationExceptionIsDueToNullField(exception, Customer.class, "name");
    }

    @Test
    void testSaveCustomerWhenNameIsEmptyShouldThrowException() {
        customerRepository.save(DomainDataGenerator.generateDefaultCustomerBuilder().name("").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> customerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Customer.class, "name");
    }

    @Test
    void testSaveCustomerWhenNameIsComposedOfSpacesShouldThrowException() {
        customerRepository.save(DomainDataGenerator.generateDefaultCustomerBuilder().name(" ").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> customerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Customer.class, "name");
    }

    @Test
    void testSaveCustomerWhenNameIsTooLongShouldThrowException() {
        customerRepository.save(DomainDataGenerator.generateDefaultCustomerBuilder().name(RandomStringUtils.secure().nextAlphanumeric(51)).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> customerRepository.flush());
        checkValidationExceptionIsDueToFieldStringSize(exception, Customer.class, "name");
    }

}
