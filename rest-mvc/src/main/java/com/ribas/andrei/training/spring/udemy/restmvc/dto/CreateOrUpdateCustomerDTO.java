package com.ribas.andrei.training.spring.udemy.restmvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrUpdateCustomerDTO {
    @NotBlank
    @NotNull
    private String name;
}
