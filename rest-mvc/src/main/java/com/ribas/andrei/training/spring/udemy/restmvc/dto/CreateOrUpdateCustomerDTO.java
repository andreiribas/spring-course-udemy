package com.ribas.andrei.training.spring.udemy.restmvc.dto;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.NullOrNotBlank;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.OnCreate;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.OnPatch;
import com.ribas.andrei.training.spring.udemy.restmvc.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrUpdateCustomerDTO {
    @Size(max = 50, groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @NullOrNotBlank(groups = OnPatch.class)
    @Setter
    private String name;
}
