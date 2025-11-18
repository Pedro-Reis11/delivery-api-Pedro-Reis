package com.delivery_api.Projeto.Delivery.API.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta contendo os dados do cliente")
public class ClienteResponseDTO {

    @Schema(description = "Identificador único do cliente", example = "10")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "João da Silva")
    private String nome;

    @Schema(description = "Email do cliente", example = "joao@email.com")
    private String email;

    @Schema(description = "Telefone de contato do cliente", example = "(11) 98765-4321")
    private String telefone;

    @Schema(description = "Endereço completo do cliente", example = "Rua das Flores, 123 - SP")
    private String endereco;

    @Schema(description = "Indica se o cliente está ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Data de cadastro do cliente", example = "2025-11-18T14:30:00")
    private LocalDateTime dataCadastro;
}
