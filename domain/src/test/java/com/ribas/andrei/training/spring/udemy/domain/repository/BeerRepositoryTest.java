package com.ribas.andrei.training.spring.udemy.domain.repository;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTestImpl
class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        var beer = beerRepository.save(Beer.builder()
                        .name("Test Beer")
                        .style("Lager")
                        .quantity(147)
                        .upc("123456789012")
                        .price(BigDecimal.valueOf(10.99))
                .build());

        beerRepository.flush();
        assertNotNull(beer);
        assertNotNull(beer.getId());
    }

}
