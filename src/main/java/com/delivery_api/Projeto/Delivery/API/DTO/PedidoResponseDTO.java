package com.delivery_api.Projeto.Delivery.API.DTO;

import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private Long id;
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private LocalDateTime dataCriacao;
    private PedidoStatus status;
    private BigDecimal total;
    private String observacoes;
    private String enderecoEntrega;

    // Dados do cliente
    private Long clienteId;
    private String clienteNome;
    private String clienteEmail;
    private String clienteTelefone;

    // Dados do restaurante
    private Long restauranteId;
    private String restauranteNome;
    private String restauranteCategoria;
    private BigDecimal restauranteTaxaEntrega;

    // Itens do save
    private List<ItemPedidoResponseDTO> itens;

    public PedidoResponseDTO(Pedido save) {
        this.id = save.getId();
        this.numeroPedido = save.getNumeroPedido();
        this.dataPedido = save.getDataPedido();
        this.dataCriacao = save.getDataCriacao();
        this.status = save.getStatus();
        this.total = save.getTotal();
        this.observacoes = save.getObservacoes();
        this.enderecoEntrega = save.getEnderecoEntrega();
        this.clienteId = save.getCliente().getId();
        this.clienteNome = save.getCliente().getNome();
        this.clienteEmail = save.getCliente().getEmail();
        this.clienteTelefone = save.getCliente().getTelefone();
        this.restauranteId = save.getRestaurante().getId();
        this.restauranteNome = save.getRestaurante().getNome();
        this.restauranteCategoria = save.getRestaurante().getCategoria();
        this.restauranteTaxaEntrega = save.getRestaurante().getTaxaEntrega();
        this.itens = save.getItens().stream().map(ItemPedidoResponseDTO::new).toList();
    }

}
