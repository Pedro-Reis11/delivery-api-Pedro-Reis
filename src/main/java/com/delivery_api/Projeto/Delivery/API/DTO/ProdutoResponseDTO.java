package com.delivery_api.Projeto.Delivery.API.DTO;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private Boolean disponivel;
    private String restauranteNome;
    private Long restauranteId;

    public ProdutoResponseDTO (Produto save){
        this.id = save.getId();
        this.nome = save.getNome();
        this.descricao = save.getDescricao();
        this.preco = save.getPreco();
        this.categoria = save.getCategoria();
        this.disponivel = save.isDisponivel();
        this.restauranteNome = save.getRestaurante().getNome();
        this.restauranteId = save.getRestaurante().getId();
    }
}
