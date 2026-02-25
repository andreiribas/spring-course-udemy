package com.ribas.andrei.training.spring.udemy.domain.repository;

import com.ribas.andrei.training.spring.udemy.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
