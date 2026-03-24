package com.ribas.andrei.training.spring.udemy.domain.test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanValidationUtils {

    private BeanValidationUtils() {}

    public static void checkValidationExceptionIsDueToNullField(ConstraintViolationException exception, Class<?> clazz, String fieldName) {
        checkValidationExceptionIsDueToFieldAndMessageTemplate(exception, clazz, fieldName, "{jakarta.validation.constraints.NotNull.message}");
    }

    public static void checkValidationExceptionIsDueToBlankStringField(ConstraintViolationException exception, Class<?> clazz, String fieldName) {
        checkValidationExceptionIsDueToFieldAndMessageTemplate(exception, clazz, fieldName, "{jakarta.validation.constraints.NotBlank.message}");
    }

    public static void checkValidationExceptionIsDueToFieldStringSize(ConstraintViolationException exception, Class<?> clazz, String fieldName) {
        checkValidationExceptionIsDueToFieldAndMessageTemplate(exception, clazz, fieldName, "{jakarta.validation.constraints.Size.message}");
    }

    public static void checkValidationExceptionIsDueToNegativeNumberField(ConstraintViolationException exception, Class<?> clazz, String fieldName) {
        checkValidationExceptionIsDueToFieldAndMessageTemplate(exception, clazz, fieldName, "{jakarta.validation.constraints.PositiveOrZero.message}");
    }

    public static void checkValidationExceptionIsDueToNonPositiveNumberField(ConstraintViolationException exception, Class<?> clazz, String fieldName) {
        checkValidationExceptionIsDueToFieldAndMessageTemplate(exception, clazz, fieldName, "{jakarta.validation.constraints.Positive.message}");
    }


    public static void checkValidationExceptionIsDueToFieldAndMessageTemplate(ConstraintViolationException exception, Class<?> clazz, String fieldName, String messageTemplate) {
        var violationOptional = getFirstViolationSortedByMessageTemplateAlphabeticallyStartingByNotNull(exception);
        assertTrue(violationOptional.isPresent());
        var violation = violationOptional.get();
        assertEquals(fieldName, violation.getPropertyPath().toString());
        assertEquals(clazz, violation.getRootBeanClass());
        assertEquals(messageTemplate, violation.getMessageTemplate());
    }

    public static Optional<ConstraintViolation<?>> getFirstViolationSortedByMessageTemplateAlphabeticallyStartingByNotNull(ConstraintViolationException exception) {
        return getViolationsStreamSortedByMessageTemplateAlphabeticallyStartingByNotNull(exception).findFirst();
    }

    public static Stream<ConstraintViolation<?>> getViolationsStreamSortedByMessageTemplateAlphabeticallyStartingByNotNull(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
                .sorted(
                        Comparator.comparing((ConstraintViolation<?> v) -> v.getLeafBean().getClass().getName())
                                .thenComparing(v -> v.getPropertyPath().toString())
                                .thenComparing(v -> !"{jakarta.validation.constraints.NotNull.message}".equals(v.getMessageTemplate()))
                );
    }

    public static Optional<ConstraintViolation<?>> getNotNullOrFirstAlphabeticallyValidationProperty(ConstraintViolationException exception) {
        return exception.getConstraintViolations().stream()
                .sorted(
                        Comparator.comparing((ConstraintViolation<?> v) -> v.getLeafBean().getClass().getName())
                                .thenComparing(v -> v.getPropertyPath().toString())
                                .thenComparing(v -> !"{jakarta.validation.constraints.NotNull.message}".equals(v.getMessageTemplate()))
                )
                .findFirst();
    }
}
