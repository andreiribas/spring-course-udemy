package com.ribas.andrei.training.spring.udemy.domain.repository;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.domain.test.DomainDataGenerator;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

import static com.ribas.andrei.training.spring.udemy.domain.test.BeanValidationUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTestImpl
class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Transactional
    @Rollback
    @Test
    void testSaveBeer() {
        var beer = beerRepository.save(DomainDataGenerator.generateDefaultBeer());
        beerRepository.flush();
        assertNotNull(beer);
        assertNotNull(beer.getId());
    }

    @Transactional
    @Rollback
    @Test
    void testSaveBeerV2() {
        var beer = beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder()
                .name(RandomStringUtils.secure().nextAlphanumeric(50))
                .style(RandomStringUtils.secure().nextAlphanumeric(50))
                .quantity(0)
                .upc(RandomStringUtils.secure().nextAlphanumeric(255))
                .price(new BigDecimal("0.1"))
                .build());
        beerRepository.flush();
        assertNotNull(beer);
        assertNotNull(beer.getId());
    }

    // name field validation errors

    @Test
    void testSaveBeerWhenNameIsNullShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().name(null).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNullField(exception, Beer.class, "name");
    }

    @Test
    void testSaveBeerWhenNameIsEmptyShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().name("").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Beer.class, "name");
    }

    @Test
    void testSaveBeerWhenNameIsComposedOfSpacesShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().name(" ").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Beer.class, "name");
    }

    @Test
    void testSaveBeerWhenNameIsTooLongShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().name(RandomStringUtils.secure().nextAlphanumeric(51)).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToFieldStringSize(exception, Beer.class, "name");
    }

    // style field validation errors

    @Test
    void testSaveBeerWhenStyleIsNullShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().style(null).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNullField(exception, Beer.class, "style");
    }

    @Test
    void testSaveBeerWhenStyleIsEmptyShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().style("").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Beer.class, "style");
    }

    @Test
    void testSaveBeerWhenStyleIsComposedOfSpacesShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().style(" ").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Beer.class, "style");
    }

    @Test
    void testSaveBeerWhenStyleIsTooLongShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().style(RandomStringUtils.secure().nextAlphanumeric(51)).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToFieldStringSize(exception, Beer.class, "style");
    }

    // quantity field validation errors

    @Test
    void testSaveBeerWhenQuantityIsNullShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().quantity(null).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNullField(exception, Beer.class, "quantity");
    }

    @Test
    void testSaveBeerWhenQuantityIsNegativeShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().quantity(-1).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNegativeNumberField(exception, Beer.class, "quantity");
    }

    // upc field validation errors

    @Test
    void testSaveBeerWhenUpcIsNullShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().upc(null).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNullField(exception, Beer.class, "upc");
    }

    @Test
    void testSaveBeerWhenUpcIsEmptyShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().upc("").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Beer.class, "upc");
    }

    @Test
    void testSaveBeerWhenUpcIsComposedOfSpacesShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().upc(" ").build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToBlankStringField(exception, Beer.class, "upc");
    }

    @Test
    void testSaveBeerWhenUpcIsTooLongShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().upc(RandomStringUtils.secure().nextAlphanumeric(256)).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToFieldStringSize(exception, Beer.class, "upc");
    }

    // price field validation errors

    @Test
    void testSaveBeerWhenPriceIsNullShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().price(null).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNullField(exception, Beer.class, "price");
    }

    @Test
    void testSaveBeerWhenPriceIsNegativeShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().price(new BigDecimal(-1)).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNonPositiveNumberField(exception, Beer.class, "price");
    }

    @Test
    void testSaveBeerWhenPriceIsNegativeShouldThrowExceptionV2() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().price(new BigDecimal("-0.1")).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNonPositiveNumberField(exception, Beer.class, "price");
    }

    @Test
    void testSaveBeerWhenPriceIsZeroShouldThrowException() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().price(new BigDecimal(0)).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNonPositiveNumberField(exception, Beer.class, "price");
    }

    @Test
    void testSaveBeerWhenPriceIsZeroShouldThrowExceptionV2() {
        beerRepository.save(DomainDataGenerator.generateDefaultBeerBuilder().price(new BigDecimal("0.0")).build());
        var exception = assertThrows(ConstraintViolationException.class, () -> beerRepository.flush());
        checkValidationExceptionIsDueToNonPositiveNumberField(exception, Beer.class, "price");
    }

}
