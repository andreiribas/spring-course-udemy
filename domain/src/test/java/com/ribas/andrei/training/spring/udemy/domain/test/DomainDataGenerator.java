package com.ribas.andrei.training.spring.udemy.domain.test;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.domain.model.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DomainDataGenerator {

    private DomainDataGenerator() {}

    public static Beer generateDefaultBeer() {
        return generateDefaultBeer(false);
    }

    public static Beer generateDefaultBeer(boolean withId) {
        return generateDefaultBeerBuilder(withId).build();
    }

    public static Beer.BeerBuilder generateDefaultBeerBuilder() {
        return generateDefaultBeerBuilder(false);
    }

    public static Beer.BeerBuilder generateDefaultBeerBuilder(boolean withId) {
        var now = LocalDateTime.now();
        return Beer.builder().id(withId ? UUID.randomUUID() : null)
                .name("Beericious")
                .style("Lager")
                .quantity(150)
                .upc("123456789012")
                .price(BigDecimal.valueOf(2.99))
                .createdAt(now)
                .updatedAt(now);
    }

    public static Customer generateDefaultCustomer() {
        return generateDefaultCustomer(false);
    }

    public static Customer generateDefaultCustomer(boolean withId) {
        return generateDefaultCustomerBuilder(withId).build();
    }

    public static Customer.CustomerBuilder generateDefaultCustomerBuilder() {
        return generateDefaultCustomerBuilder(false);
    }

    public static Customer.CustomerBuilder generateDefaultCustomerBuilder(boolean withId) {
        var now = LocalDateTime.now();
        return Customer.builder().id(withId ? UUID.randomUUID() : null)
                .name("John Doe")
                .createdAt(now)
                .updatedAt(now);
    }


}
