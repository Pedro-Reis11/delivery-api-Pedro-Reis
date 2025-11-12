package com.delivery_api.Projeto.Delivery.API.DTO.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de criação ou atualização de restaurante", title = "Restaurante Request DTO")
public class RestauranteRequestDTO {

    @Schema(description = "Identificador único do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria Sabor & Arte", required = true)
    @NotBlank(message = "O nome do restaurante é obrigatório")
    private String nome;

    @Schema(description = "Categoria culinária do restaurante", example = "Italiana", required = true)
    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @Schema(description = "Endereço completo do restaurante", example = "Rua das Flores, 123 - São Paulo, SP", required = true)
    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    @Schema(description = "Telefone de contato do restaurante", example = "(11) 98765-4321", required = true)
    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @Schema(description = "Taxa de entrega cobrada pelo restaurante", example = "8.50", required = true)
    @NotNull(message = "A taxa de entrega é obrigatória")
    private BigDecimal taxaEntrega;

    @Schema(description = "Avaliação média do restaurante", example = "4.5")
    private BigDecimal avaliacao;

    @Schema(description = "Indica se o restaurante está ativo", example = "true", required = true)
    @NotNull(message = "O campo 'ativo' é obrigatório")
    private Boolean ativo;
}

