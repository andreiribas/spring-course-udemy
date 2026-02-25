package com.ribas.andrei.training.spring.udemy.domain.repository;

import com.ribas.andrei.training.spring.udemy.domain.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
