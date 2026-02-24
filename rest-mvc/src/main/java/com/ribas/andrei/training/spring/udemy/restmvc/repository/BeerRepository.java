package com.ribas.andrei.training.spring.udemy.restmvc.repository;

import com.ribas.andrei.training.spring.udemy.restmvc.model.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
