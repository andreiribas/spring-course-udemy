package com.ribas.andrei.training.spring.udemy.restmvc.dto;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.NullOrNotBlank;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.OnCreate;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.OnPatch;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrUpdateBeerDTO {

    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @NullOrNotBlank(groups = OnPatch.class)
    private String name;

    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @NullOrNotBlank(groups = OnPatch.class)
    private String style;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Integer quantity;

    @Size(max = 255, groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @NullOrNotBlank(groups = OnPatch.class)
    private String upc;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @Positive(groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private BigDecimal price;

}
