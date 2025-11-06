package com.delivery_api.Projeto.Delivery.API.DTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemPedidoRequestDTO {
    @NotNull(message = "Produto é obrigatório")
    private Long produtoId;
    @NotNull(message = "Quantidade é obrigatório")
    private Integer quantidade;
    private String observacoes;  // Opcional: "Sem cebola", etc.
}
