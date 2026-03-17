package com.ribas.andrei.training.spring.udemy.restmvc.exception;

import com.ribas.andrei.training.spring.udemy.restmvc.dto.BeerDTO;
import com.ribas.andrei.training.spring.udemy.restmvc.exception.dto.InvalidFieldDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<List<InvalidFieldDTO>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation exception: {}", e.getMessage());
        var errorList = e.getFieldErrors().stream().map(fieldError -> new InvalidFieldDTO(fieldError.getObjectName(), fieldError.getField(), fieldError.getCode()))
                .sorted(Comparator.comparing(InvalidFieldDTO::objectName).thenComparing(InvalidFieldDTO::fieldName).thenComparing(InvalidFieldDTO::code))
                .toList();
        return ResponseEntity.badRequest().body(errorList);
    }
}
