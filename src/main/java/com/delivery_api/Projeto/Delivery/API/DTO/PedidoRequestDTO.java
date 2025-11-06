package com.delivery_api.Projeto.Delivery.API.DTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    @NotNull(message = "Restaurante é obrigatório")
    private Long restauranteId;
    private List<ItemPedidoRequestDTO> itens;
    @NotNull(message = "Endereço é obrigatório")
    private String enderecoEntrega;
    private String observacoes;
}
