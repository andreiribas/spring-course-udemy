package com.ribas.andrei.training.spring.udemy.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String style;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @NotNull
    @NotBlank
    private String upc;

    @NotNull
    @Positive
    private BigDecimal price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
