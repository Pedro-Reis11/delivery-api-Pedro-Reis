package com.delivery_api.Projeto.Delivery.API.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurantes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String categoria;

    private String endereco;

    private String telefone;  // Campo adicionado

    @Column(precision = 10, scale = 2)
    private BigDecimal taxaEntrega;  // Campo adicionado

    @Column(precision = 2, scale = 1)
    private BigDecimal avaliacao;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    private Boolean ativo = true;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"restaurante", "hibernateLazyInitializer", "handler"})
    private List<Produto> produtos;

    public void inativar() {
        this.ativo = false;
    }

    public boolean isAtivo() {
        return ativo != null && ativo;
    }

    }