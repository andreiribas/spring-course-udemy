package com.ribas.andrei.training.spring.udemy.restmvc.exception;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ResponseEntity.badRequest().body(e.getBindingResult().getFieldErrors());
    }
}
