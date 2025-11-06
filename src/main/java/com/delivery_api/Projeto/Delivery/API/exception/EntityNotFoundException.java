package com.delivery_api.Projeto.Delivery.API.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException(String entity, Long id) {
        super(entity + " não encontrado(a) com ID: " + id);
    }
}
