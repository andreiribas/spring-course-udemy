package com.ribas.andrei.training.spring.udemy.domain.repository;


import com.ribas.andrei.training.spring.udemy.domain.TestConfig;
import com.ribas.andrei.training.spring.udemy.domain.model.Customer;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Documented
@Retention(RUNTIME)
@Import(TestConfig.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = CustomerRepository.class)
@EntityScan(basePackageClasses = Customer.class)
public @interface DataJpaTestImpl {
}
