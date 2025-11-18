package com.delivery_api.Projeto.Delivery.API.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para criação ou atualização de restaurante", title = "Restaurante Request DTO")
public class RestauranteRequestDTO {

    @Schema(description = "Nome do restaurante", example = "Pizzaria Sabor & Arte", required = true)
    @NotBlank(message = "O nome do restaurante é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Categoria culinária do restaurante", example = "Italiana", required = true)
    @NotBlank(message = "A categoria é obrigatória")
    @Size(min = 3, max = 50, message = "A categoria deve ter entre 3 e 50 caracteres")
    private String categoria;

    @Schema(description = "CEP do restaurante", example = "12345-678", required = true)
    @NotNull(message = "O CEP do restaurante é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "O CEP deve estar no formato 12345-678 ou 12345678")
    private String cep;

    @Schema(description = "Endereço completo do restaurante",
            example = "Rua das Flores, 123 - São Paulo, SP",
            required = true)
    @NotBlank(message = "O endereço é obrigatório")
    @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres")
    private String endereco;

    @Schema(description = "Telefone de contato do restaurante",
            example = "(11) 98765-4321",
            required = true)
    @NotBlank(message = "O telefone é obrigatório")
    @Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres")
    private String telefone;

    @Schema(description = "Taxa de entrega cobrada pelo restaurante",
            example = "8.50",
            required = true)
    @NotNull(message = "A taxa de entrega é obrigatória")
    private BigDecimal taxaEntrega;

    @Schema(description = "Avaliação do restaurante", example = "4.5")
    private BigDecimal avaliacao;

}
