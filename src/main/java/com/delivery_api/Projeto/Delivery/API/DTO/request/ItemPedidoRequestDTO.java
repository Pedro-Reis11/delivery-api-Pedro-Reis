package com.delivery_api.Projeto.Delivery.API.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para requisição de inclusão de item em um pedido",
        title = "Item Pedido Request DTO")
public class ItemPedidoRequestDTO {

    @Schema(description = "Identificador do produto que será adicionado ao pedido",
            example = "15", required = true)
    @NotNull(message = "O ID do produto é obrigatório")
    @Positive(message = "O ID do produto deve ser um número positivo")
    private Long produtoId;

    @Schema(description = "Quantidade do produto no pedido",
            example = "2", required = true)
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private Integer quantidade;

    @Schema(description = "Observações adicionais sobre o item",
            example = "Sem cebola, por favor")
    @Size(max = 255, message = "As observações podem ter no máximo 255 caracteres")
    private String observacoes;
}
