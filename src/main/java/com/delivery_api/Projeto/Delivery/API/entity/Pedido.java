package com.delivery_api.Projeto.Delivery.API.entity;

import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
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
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    private BigDecimal total;

    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;


    public void alterarStatus(PedidoStatus novoStatus) {
        this.status = novoStatus;
    }

}
