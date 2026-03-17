package com.ribas.andrei.training.spring.udemy.restmvc.exception;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BeerDTO> handleNotFoundException(NotFoundException e) {
        log.error("Not found exception: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldError>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation exception: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getBindingResult().getFieldErrors().stream().peek(fieldError -> {
            // sort codes before sending, the biggest first
            String[] codes = fieldError.getCodes();
            if (ArrayUtils.isNotEmpty(codes)) {
                Arrays.sort(codes, Comparator.<String>naturalOrder().reversed());
            }
        }).sorted(Comparator.comparing(FieldError::getObjectName).thenComparing(FieldError::getField).thenComparing((f1, f2) -> {
            // sort codes, the biggest first
            if(ArrayUtils.isEmpty(f1.getCodes())) {
                return 1;
            } else if(ArrayUtils.isEmpty(f2.getCodes())) {
                return -1;
            }
            return f2.getCodes()[0].compareTo(f1.getCodes()[0]);
        })).toList());
    }
}
