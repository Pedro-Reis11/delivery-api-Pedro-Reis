package com.delivery_api.Projeto.Delivery.API.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO para criação ou atualização de produto", title = "Produto Request DTO")
public class ProdutoRequestDTO {

    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Descrição detalhada do produto",
            example = "Pizza tradicional com molho de tomate e mussarela",
            required = true)
    @NotBlank(message = "A descrição do produto é obrigatória")
    @Size(min = 5, max = 255, message = "A descrição deve ter entre 5 e 255 caracteres")
    private String descricao;

    @Schema(description = "Preço do produto", example = "39.90", required = true)
    @NotNull(message = "O preço do produto é obrigatório")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", example = "Pizzas", required = true)
    @NotBlank(message = "A categoria do produto é obrigatória")
    @Size(min = 3, max = 50, message = "A categoria deve ter entre 3 e 50 caracteres")
    private String categoria;

    @Schema(description = "Indica se o produto está disponível para venda (apenas para updates)",
            example = "true")
    private Boolean disponivel;

    @Schema(description = "Identificador único do restaurante", example = "1", required = true)
    @NotNull(message = "Produto deve estar associado a um restaurante")
    private Long restauranteId;
}
