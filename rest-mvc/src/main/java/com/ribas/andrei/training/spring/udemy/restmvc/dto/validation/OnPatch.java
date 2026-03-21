package com.ribas.andrei.training.spring.udemy.restmvc.dto.validation;

/**
 * Marker interface used for validation groups specific to the Patch operation.
 *
 * This interface is employed in conjunction with the Jakarta Bean Validation API
 * to apply conditional validation rules for the fields of DTO objects when
 * performing a partial update (HTTP PATCH).
 *
 * For example, this interface is referenced in {@code CreateOrUpdateBeerDTO}'s
 * validation annotations to enforce or relax certain validation constraints
 * during a patch operation in comparison to other operations like Create or Update.
 *
 * Typically used in combination with the {@code @Validated} annotation in
 * controller methods to trigger group-specific validation.
 */
public interface OnPatch {}