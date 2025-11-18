package com.delivery_api.Projeto.Delivery.API.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    @Override
    public void initialize(ValidTelefone constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }

        // Remove espaços e caracteres não numéricos
        String clean = telefone.replaceAll("\\D", "");

        // Telefone brasileiro padrão: 10 a 11 dígitos
        // Exemplo: 11987654321 ou 1187654321
        return clean.matches("^\\d{10,11}$");
    }
}
