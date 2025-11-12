package com.delivery_api.Projeto.Delivery.API.DTO.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de criação ou atualização de pedido", title = "Pedido Request DTO")
public class PedidoRequestDTO {

    @Schema(description = "Identificador único do pedido", example = "1")
    private Long id;

    @Schema(description = "Número identificador do pedido", example = "PED123456", required = true)
    @NotBlank(message = "O número do pedido é obrigatório")
    private String numeroPedido;

    @Schema(description = "Data e hora em que o pedido foi realizado", example = "2025-11-12T14:30:00", required = true)
    @NotNull(message = "A data do pedido é obrigatória")
    private LocalDateTime dataPedido;

    @Schema(description = "Status atual do pedido", example = "EM_PREPARO", required = true)
    @NotBlank(message = "O status do pedido é obrigatório")
    private String status;

    @Schema(description = "Valor total do pedido", example = "89.90", required = true)
    @NotNull(message = "O valor total é obrigatório")
    private BigDecimal valorTotal;

    @Schema(description = "Observações adicionais do pedido", example = "Sem cebola, por favor")
    private String observacoes;

    @Schema(description = "ID do cliente associado ao pedido", example = "10", required = true)
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @Schema(description = "ID do restaurante associado ao pedido", example = "5", required = true)
    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    @Schema(description = "Itens do pedido em formato JSON ou string", example = "[{\"itemId\":1,\"quantidade\":2}]")
    @NotBlank(message = "Os itens do pedido são obrigatórios")
    private String itens;
}
