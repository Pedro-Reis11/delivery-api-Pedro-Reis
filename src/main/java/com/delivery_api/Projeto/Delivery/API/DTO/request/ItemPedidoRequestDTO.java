package com.delivery_api.Projeto.Delivery.API.DTO.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para requisição de inclusão de item em um pedido", title = "Item Pedido Request DTO")
public class ItemPedidoRequestDTO {

    @Schema(description = "Identificador do produto que será adicionado ao pedido", example = "15", required = true)
    @NotNull(message = "O ID do produto é obrigatório")
    private Long produtoId;

    @Schema(description = "Quantidade do produto no pedido", example = "2", required = true)
    @NotNull(message = "A quantidade é obrigatória")
    private Integer quantidade;

    @Schema(description = "Observações adicionais sobre o item", example = "Sem cebola, por favor")
    private String observacoes;
}

