package com.delivery_api.Projeto.Delivery.API.DTO.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisição de criação ou atualização de produto", title = "Produto Request DTO")
public class ProdutoRequestDTO {

    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Pizza tradicional com molho de tomate e mussarela", required = true)
    @NotBlank(message = "A descrição do produto é obrigatória")
    private String descricao;

    @Schema(description = "Preço do produto", example = "39.90", required = true)
    @NotNull(message = "O preço do produto é obrigatório")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", example = "Pizzas", required = true)
    @NotBlank(message = "A categoria do produto é obrigatória")
    private String categoria;

    @Schema(description = "Indica se o produto está disponível para venda", example = "true", required = true)
    @NotNull(message = "O campo de disponibilidade é obrigatório")
    private Boolean disponivel;
}

