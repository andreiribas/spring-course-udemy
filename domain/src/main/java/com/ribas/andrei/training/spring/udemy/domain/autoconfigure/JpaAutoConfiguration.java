package com.ribas.andrei.training.spring.udemy.domain.autoconfigure;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import com.ribas.andrei.training.spring.udemy.domain.repository.BeerRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@EnableJpaRepositories(basePackageClasses = BeerRepository.class)
@EntityScan(basePackageClasses = Beer.class)
public class JpaAutoConfiguration {
}
