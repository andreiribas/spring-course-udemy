package com.ribas.andrei.training.spring.udemy.restmvc.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
