package com.delivery_api.Projeto.Delivery.API.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta contendo os dados de um item do pedido")
public class ItemPedidoResponseDTO {

    @Schema(description = "Identificador único do item do pedido", example = "1")
    private Long id;

    @Schema(description = "ID do produto associado ao item", example = "15")
    private Long produtoId;

    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String produtoNome;

    @Schema(description = "Quantidade do produto solicitada no pedido", example = "2")
    private Integer quantidade;

    @Schema(description = "Valor unitário do produto no momento do pedido", example = "39.90")
    private BigDecimal precoUnitario;

    @Schema(description = "Subtotal calculado (preço x quantidade)", example = "79.80")
    private BigDecimal subtotal;

    @Schema(description = "Observações adicionais sobre o item", example = "Sem cebola")
    private String observacoes;
}
