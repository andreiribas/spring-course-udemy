package com.ribas.andrei.training.spring.udemy.restmvc.dto.validation;

/**
 * Marker interface used for validation groups specific to the Update operation.
 *
 * This interface is utilized in conjunction with the Jakarta Bean Validation API
 * to apply specific validation rules when updating entities through DTOs
 * in an HTTP PUT or similar operation.
 *
 * For example, this interface is referenced in {@code CreateOrUpdateBeerDTO}'s
 * validation annotations to enforce stricter constraints during an update operation
 * compared to other operations like a partial update or creation.
 *
 * Typically used with the {@code @Validated} annotation in controller methods
 * to trigger group-specific validation conditions.
 */
public interface OnUpdate {}

