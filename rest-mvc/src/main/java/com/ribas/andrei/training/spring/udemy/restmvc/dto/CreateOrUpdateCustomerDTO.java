package com.ribas.andrei.training.spring.udemy.restmvc.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrUpdateCustomerDTO {
    private String name;
}
