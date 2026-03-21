package com.ribas.andrei.training.spring.udemy.restmvc.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a string is either null or not blank.
 * Intended for use with the {@link OnPatch} group, where a field being absent (null)
 * is acceptable, but an explicitly provided empty or whitespace-only value is not.
 */
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlank {
    String message() default "must not be blank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
