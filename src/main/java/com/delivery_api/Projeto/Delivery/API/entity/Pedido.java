package com.delivery_api.Projeto.Delivery.API.entity;

import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroPedido;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal total;  // Mapeado para valor_total

    private String observacoes;

    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @JsonIgnoreProperties({"produtos", "hibernateLazyInitializer", "handler"})
    private Restaurante restaurante;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"pedido", "hibernateLazyInitializer", "handler"})
    private List<ItemPedido> itens = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    @JsonIgnoreProperties({"restaurante", "hibernateLazyInitializer", "handler"})
    private List<Produto> produtos;

    public void alterarStatus(PedidoStatus novoStatus) {
        this.status = novoStatus;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
    }

    public void calcularTotal() {
        BigDecimal totalItens = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxaEntrega = (restaurante != null && restaurante.getTaxaEntrega() != null)
                ? restaurante.getTaxaEntrega()
                : BigDecimal.ZERO;

        this.total = totalItens.add(taxaEntrega);
    }
}