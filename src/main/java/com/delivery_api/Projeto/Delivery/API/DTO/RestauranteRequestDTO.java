package com.delivery_api.Projeto.Delivery.API.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteRequestDTO {
    @NotBlank(message = "Nome do restaurante é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(min = 10, max = 200, message = "Endereço deve ter entre 10 e 200 caracteres")
    private String endereco;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4}-\\d{4}",
            message = "Telefone deve estar no formato (XX) XXXX-XXXX")
    private String telefone;

    @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser maior ou igual a zero")
    private BigDecimal taxaEntrega;

}
