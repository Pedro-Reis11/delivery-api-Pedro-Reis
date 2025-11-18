package com.delivery_api.Projeto.Delivery.API.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "DTO para criação de pedido", title = "Pedido Request DTO")
public class PedidoRequestDTO {

    @Schema(description = "ID do cliente que está realizando o pedido",
            example = "10", required = true)
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @Schema(description = "ID do restaurante no qual o pedido será realizado",
            example = "5", required = true)
    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    @Schema(description = "Endereço de entrega do pedido",
            example = "Rua das Flores, 123 - São Paulo",
            required = true)
    @NotBlank(message = "O endereço de entrega é obrigatório")
    @Size(min = 5, max = 255, message = "O endereço deve ter entre 5 e 255 caracteres")
    private String enderecoEntrega;

    @Schema(description = "Observações adicionais do pedido",
            example = "Sem cebola, por favor")
    @Size(max = 255, message = "As observações podem ter no máximo 255 caracteres")
    private String observacoes;

    @Schema(description = "Lista de itens do pedido", required = true)
    @NotNull(message = "Os itens do pedido são obrigatórios")
    @Size(min = 1, message = "O pedido deve conter pelo menos um item")
    private List<ItemPedidoRequestDTO> itens;
}
