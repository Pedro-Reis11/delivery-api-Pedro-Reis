package com.delivery_api.Projeto.Delivery.API.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
