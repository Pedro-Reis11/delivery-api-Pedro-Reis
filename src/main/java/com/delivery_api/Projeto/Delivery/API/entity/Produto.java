package com.delivery_api.Projeto.Delivery.API.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produtos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;  // Campo adicionado

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    private String categoria;  // Campo adicionado

    private Boolean disponivel = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @JsonIgnoreProperties({"produtos", "hibernateLazyInitializer", "handler"})
    private Restaurante restaurante;

    public boolean isAtivo() {
        return this.disponivel != null && this.disponivel;
    }
}