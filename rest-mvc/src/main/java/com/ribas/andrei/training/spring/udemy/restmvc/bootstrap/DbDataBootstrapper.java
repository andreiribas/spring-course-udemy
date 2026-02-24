package com.ribas.andrei.training.spring.udemy.restmvc.bootstrap;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import com.ribas.andrei.training.spring.udemy.restmvc.model.Customer;
import com.ribas.andrei.training.spring.udemy.restmvc.repository.BeerRepository;
import com.ribas.andrei.training.spring.udemy.restmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DbDataBootstrapper implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String[] _args) {
        log.info("Loading data into the db.");
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {
        if(beerRepository.count() > 0) {
            return;
        }
        var beers = List.of(
            Beer.builder().name("Galaxy Cat").style("PALE_ALE").upc("12356")
                .price(new BigDecimal("12.99")).quantity(122).createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build(),
            Beer.builder().name("Crank").style("PALE_ALE").upc("12356222")
                .price(new BigDecimal("11.99")).quantity(392).createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build(),
            Beer.builder().name("Sunshine City").style("IPA").upc("12356")
                .price(new BigDecimal("13.99")).quantity(144)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build()
        );
        beerRepository.saveAll(beers);
    }

    private void loadCustomerData() {
        if(customerRepository.count() > 0) {
            return;
        }
        var customers = List.of(
            Customer.builder().name("James Smith").createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build(),
            Customer.builder().name("John Doe").createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build()
        );
        customerRepository.saveAll(customers);
    }
}
