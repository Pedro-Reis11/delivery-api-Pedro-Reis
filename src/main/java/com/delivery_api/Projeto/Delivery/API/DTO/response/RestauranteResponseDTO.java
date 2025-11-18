package com.delivery_api.Projeto.Delivery.API.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta contendo os dados de um restaurante")
public class RestauranteResponseDTO {

    @Schema(description = "ID do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria Sabor & Arte")
    private String nome;

    @Schema(description = "Categoria culinária", example = "Italiana")
    private String categoria;

    @Schema(description = "Endereço completo do restaurante", example = "Rua das Flores, 123 - SP")
    private String endereco;

    @Schema(description = "Telefone de contato", example = "(11) 98765-4321")
    private String telefone;

    @Schema(description = "Taxa de entrega", example = "8.50")
    private BigDecimal taxaEntrega;

    @Schema(description = "Avaliação média do restaurante", example = "4.6")
    private BigDecimal avaliacao;

    @Schema(description = "Indica se o restaurante está ativo", example = "true")
    private Boolean ativo;

    @Schema(description = "Data de cadastro do restaurante", example = "2025-11-12T14:30:00")
    private LocalDateTime dataCadastro;
}
