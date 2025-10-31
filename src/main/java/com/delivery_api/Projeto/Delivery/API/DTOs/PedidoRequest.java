package com.delivery_api.Projeto.Delivery.API.DTOs;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {
    private Long clienteId;
    private Long restauranteId;
    private List<ItemPedidoRequest> itens;
    private String enderecoEntrega;
    private String observacoes;
}
