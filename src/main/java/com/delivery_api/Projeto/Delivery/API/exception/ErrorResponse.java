package com.delivery_api.Projeto.Delivery.API.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp = LocalDateTime.now(); // Data/hora do erro
    private int status;           // Código HTTP (ex: 404, 400, 500)
    private String error;         // Ex: "Not Found", "Bad Request"
    private String message;       // Mensagem detalhada da exceção
    private String path;          // Endpoint que causou o erro
    private Map<String, String> errors; // Campos inválidos e mensagens (validações)
}
