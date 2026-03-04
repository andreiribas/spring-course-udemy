package com.ribas.andrei.training.spring.udemy.restmvc.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class CustomerDTO extends CreateOrUpdateCustomerDTO {
    @NonNull
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
