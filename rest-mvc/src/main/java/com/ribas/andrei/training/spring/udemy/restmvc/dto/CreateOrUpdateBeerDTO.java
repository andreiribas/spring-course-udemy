package com.ribas.andrei.training.spring.udemy.restmvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrUpdateBeerDTO {
    @NotBlank
    @NotNull
    private String name;
    private String style;
    private Integer quantity;
    private String upc;
    private BigDecimal price;
}
