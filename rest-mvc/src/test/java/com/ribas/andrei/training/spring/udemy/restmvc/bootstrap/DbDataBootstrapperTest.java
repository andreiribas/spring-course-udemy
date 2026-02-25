package com.ribas.andrei.training.spring.udemy.restmvc.bootstrap;

import com.ribas.andrei.training.spring.udemy.domain.repository.BeerRepository;
import com.ribas.andrei.training.spring.udemy.domain.repository.CustomerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.test.DbTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {
        DbDataBootstrapper.class,
        DbTestConfig.class })
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
