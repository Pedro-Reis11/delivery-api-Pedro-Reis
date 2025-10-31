package com.delivery_api.Projeto.Delivery.API.DTOs;
import lombok.Data;

@Data
public class ItemPedidoRequest {
    private Long produtoId;
    private Integer quantidade;
    private String observacoes;  // Opcional: "Sem cebola", etc.
}
