package com.delivery_api.Projeto.Delivery.API.DTO.response;

import com.delivery_api.Projeto.Delivery.API.entity.ItemPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoResponseDTO {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    private String observacoes;

    public ItemPedidoResponseDTO(ItemPedido save){
        this.id = save.getId();
        this.produtoId = save.getId();
        this.produtoNome = save.getProduto().getNome();
        this.quantidade = save.getQuantidade();
        this.precoUnitario = save.getPrecoUnitario();
        this.subtotal = save.getSubtotal();
        this.observacoes = save.getObservacoes();
    }

}
