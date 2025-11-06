package com.delivery_api.Projeto.Delivery.API.DTO;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteResponseDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String telefone;
    private BigDecimal taxaEntrega;
    private BigDecimal avaliacao;
    private Boolean ativo;
    private LocalDateTime dataCadastro;


    public RestauranteResponseDTO(Restaurante save) {
        this.id = save.getId();
        this.nome = save.getNome();
        this.categoria = save.getCategoria();
        this.endereco = save.getEndereco();
        this.telefone = save.getTelefone();
        this.taxaEntrega = save.getTaxaEntrega();
        this.avaliacao = save.getAvaliacao();
        this.ativo = save.getAtivo();
        this.dataCadastro = save.getDataCadastro();
    }

}
