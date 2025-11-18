package com.delivery_api.Projeto.Delivery.API.DTO.response;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(
        description = "Wrapper para encapsular respostas da API",
        title = "Api Response Wrapper")
public class ApiResponseWrapper<T> {

    @Schema(description = "Indica se a operação foi bem-sucedida", example = "true")
    private boolean success;

    @Schema(description = "Dados da Resposta")
    private T data;

    @Schema(description = "Mensagem de resposta", example = "Operação realizada com sucesso")
    private String message;

    @Schema(description = "Timestamp da resposta", example = "2023-10-01T12:00:00")
    private LocalDateTime timestamp;

    public ApiResponseWrapper(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponseWrapper<T> success(T data, String message) {
        return new ApiResponseWrapper<>(true, data, message);
    }

    public static <T> ApiResponseWrapper<T> error(String message) {
        return new ApiResponseWrapper<>(false, null, message);
    }

    // Getters e Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
