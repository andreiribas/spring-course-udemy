package com.ribas.andrei.training.spring.udemy.restmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.ribas.andrei.training.spring.udemy")
@EnableJpaRepositories(basePackages = "com.ribas.andrei.training.spring.udemy.domain.repository")
@EntityScan(basePackages = "com.ribas.andrei.training.spring.udemy.domain.model")
public class RestMvcSpringBootApplication {
    static void main(String[] args) {
        SpringApplication.run(RestMvcSpringBootApplication.class, args);
    }
}
