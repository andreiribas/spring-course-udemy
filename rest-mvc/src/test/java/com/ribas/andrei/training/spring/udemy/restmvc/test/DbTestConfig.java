package com.ribas.andrei.training.spring.udemy.restmvc.test;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.domain.repository.BeerRepository;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan(basePackageClasses = Beer.class)
@EnableJpaRepositories(basePackageClasses = BeerRepository.class)
public class DbTestConfig {
}
