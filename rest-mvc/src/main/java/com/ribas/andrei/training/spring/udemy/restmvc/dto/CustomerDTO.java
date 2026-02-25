package com.ribas.andrei.training.spring.udemy.restmvc.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDTO {
    private UUID id;
    private String name;
}
