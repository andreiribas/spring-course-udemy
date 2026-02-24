package com.ribas.andrei.training.spring.udemy.restmvc.bootstrap;

import com.ribas.andrei.training.spring.udemy.restmvc.repository.BeerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class DbDataBootstrapperTest {

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private DbDataBootstrapper fixture;

    @BeforeEach
    void setUp() {
        fixture = new DbDataBootstrapper(beerRepository, customerRepository);
    }

    @Test
    void testRun() {
        fixture.run(null);

        assertEquals(3, beerRepository.count());
        assertEquals(2, customerRepository.count());
    }
}
