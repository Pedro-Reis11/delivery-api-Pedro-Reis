package com.delivery_api.Projeto.Delivery.API.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoriaValidator implements ConstraintValidator<ValidCategoria, String> {

    @Override
    public void initialize(ValidCategoria constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String categoria, ConstraintValidatorContext context) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return false;
        }

        String clean = categoria.trim();
        return !clean.isEmpty();
    }
}
