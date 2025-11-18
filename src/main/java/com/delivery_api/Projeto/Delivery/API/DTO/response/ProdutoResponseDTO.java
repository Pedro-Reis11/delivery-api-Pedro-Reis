package com.delivery_api.Projeto.Delivery.API.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta contendo os dados de um produto")
public class ProdutoResponseDTO {

    @Schema(description = "ID do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Pizza com molho de tomate e mussarela")
    private String descricao;

    @Schema(description = "Preço do produto", example = "39.90")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", example = "Pizzas")
    private String categoria;

    @Schema(description = "Indica se o produto está disponível", example = "true")
    private Boolean disponivel;

    @Schema(description = "Nome do restaurante ao qual o produto pertence", example = "Pizzaria Sabor & Arte")
    private String restauranteNome;

    @Schema(description = "ID do restaurante", example = "5")
    private Long restauranteId;
}
