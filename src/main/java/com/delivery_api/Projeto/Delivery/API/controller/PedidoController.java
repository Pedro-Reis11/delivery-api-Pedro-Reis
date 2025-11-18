package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ItemPedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Criar pedido",
            description = "Cria um novo pedido no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do pedido"),
            @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO response = pedidoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar pedidos",
            description = "Lista todos os pedidos ou filtra por cliente usando o parâmetro clienteId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listar(@RequestParam(required = false) Long clienteId) {
        List<PedidoResponseDTO> pedidos =
                (clienteId != null) ? pedidoService.listarPorCliente(clienteId) : pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID",
            description = "Recupera os detalhes de um pedido específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente",
            description = "Recupera pedidos feitos por um cliente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado ou sem pedidos")
    })
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pedidos por status",
            description = "Recupera pedidos filtrados pelo status informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado com o status fornecido")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listarPorStatus(@PathVariable PedidoStatus status) {
        return ResponseEntity.ok(pedidoService.listarPorStatus(status));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido",
            description = "Atualiza os dados de um pedido existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido",
            description = "Altera o status de um pedido específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestParam PedidoStatus novoStatus) {
        return ResponseEntity.ok(pedidoService.alterarStatus(id, novoStatus));
    }

    @GetMapping("/recentes")
    @Operation(summary = "Listar pedidos recentes",
            description = "Retorna os pedidos mais recentes do sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido recente encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listarRecentes() {
        return ResponseEntity.ok(pedidoService.listarUltimosPedidos());
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar pedidos por período",
            description = "Recupera pedidos realizados dentro do intervalo informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado no período informado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(pedidoService.buscarPorPeriodo(inicio, fim));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido",
            description = "Cancela um pedido específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/calcular")
    @Operation(summary = "Calcular valor total do pedido",
            description = "Calcula o valor total de um pedido com base nos itens enviados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor total calculado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Itens inválidos")
    })
    public ResponseEntity<BigDecimal> calcularValorTotalPedido(
            @RequestBody List<ItemPedidoRequestDTO> itens) {

        BigDecimal valorTotal = pedidoService.calcularValorTotalPedido(itens);
        return ResponseEntity.ok(valorTotal);
    }
}
