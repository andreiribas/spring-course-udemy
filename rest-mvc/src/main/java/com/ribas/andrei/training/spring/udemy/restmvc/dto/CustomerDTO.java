package com.ribas.andrei.training.spring.udemy.restmvc.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String name;
}
