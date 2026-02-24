package com.ribas.andrei.training.spring.udemy.restmvc.repository;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        var beer = beerRepository.save(Beer.builder()
                        .name("Test Beer")
                .build());
        assertNotNull(beer);
        assertNotNull(beer.getId());
    }

}
