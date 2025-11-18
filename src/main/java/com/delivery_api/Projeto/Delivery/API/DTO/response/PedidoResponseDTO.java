package com.delivery_api.Projeto.Delivery.API.DTO.response;

import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta contendo todas as informações de um pedido")
public class PedidoResponseDTO {

    @Schema(description = "ID do pedido", example = "1")
    private Long id;

    @Schema(description = "Número identificador do pedido", example = "PED-123456")
    private String numeroPedido;

    @Schema(description = "Data e hora em que o pedido foi realizado", example = "2025-11-12T14:30:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Data de criação do pedido", example = "2025-11-12T14:29:59")
    private LocalDateTime dataCriacao;

    @Schema(description = "Status atual do pedido", example = "CONFIRMADO")
    private PedidoStatus status;

    @Schema(description = "Valor total do pedido", example = "89.90")
    private BigDecimal valorTotal;

    @Schema(description = "Observações adicionais do pedido", example = "Sem cebola, por favor")
    private String observacoes;

    @Schema(description = "Endereço completo de entrega", example = "Rua das Flores, 123, SP")
    private String enderecoEntrega;

    // ===================== Cliente =====================
    @Schema(description = "ID do cliente", example = "10")
    private Long clienteId;

    @Schema(description = "Nome do cliente", example = "João da Silva")
    private String clienteNome;

    @Schema(description = "Email do cliente", example = "joao@email.com")
    private String clienteEmail;

    @Schema(description = "Telefone do cliente", example = "(11) 99999-9999")
    private String clienteTelefone;

    // ===================== Restaurante =====================
    @Schema(description = "ID do restaurante", example = "4")
    private Long restauranteId;

    @Schema(description = "Nome do restaurante", example = "Pizzaria Sabor & Arte")
    private String restauranteNome;

    @Schema(description = "Categoria do restaurante", example = "Italiana")
    private String restauranteCategoria;

    @Schema(description = "Taxa de entrega cobrada pelo restaurante", example = "8.50")
    private BigDecimal restauranteTaxaEntrega;

    // ===================== Itens =====================
    @Schema(description = "Lista de itens do pedido")
    private List<ItemPedidoResponseDTO> itens;
}
