package com.ribas.andrei.training.spring.udemy.restmvc.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrUpdateBeerDTO {
    private String name;
    private String style;
    private Integer quantity;
    private String upc;
    private BigDecimal price;
}
