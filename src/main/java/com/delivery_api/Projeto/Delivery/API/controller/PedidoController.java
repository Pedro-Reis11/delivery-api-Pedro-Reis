package com.delivery_api.Projeto.Delivery.API.controller;

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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * DTO para atualização de status
     */
    record AtualizarStatusRequest(PedidoStatus status) {}

    /**
     * Criar novo pedido
     */
    @PostMapping
    @Operation(summary = "Criar um novo pedido", description = "Cadastra um novo pedido com itens e cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO response = pedidoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar todos os pedidos (ou por cliente via query param)
     */
    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna todos os pedidos cadastrados.")
    public ResponseEntity<List<PedidoResponseDTO>> listar(
            @RequestParam(required = false) Long clienteId) {
        List<PedidoResponseDTO> pedidos;

        if (clienteId != null) {
            pedidos = pedidoService.listarPorCliente(clienteId);
        } else {
            pedidos = pedidoService.listarTodos();
        }

        return ResponseEntity.ok(pedidos);
    }

    /**
     * Buscar pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    /**
     * Buscar pedido por Cliente
     */
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos de um cliente", description = "Retorna todos os pedidos realizados por um cliente específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Listar pedidos por cliente
     */
    @GetMapping("/cliente/{clienteId}/pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    /**
     * Listar pedidos por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorStatus(@PathVariable PedidoStatus status) {
        return ResponseEntity.ok(pedidoService.listarPorStatus(status));
    }

    /**
     * Atualizar pedido (observações, endereço, etc.)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizar(id, dto));
    }

    /**
     * Atualizar status do pedido
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestParam PedidoStatus novoStatus) {
        return ResponseEntity.ok(pedidoService.alterarStatus(id, novoStatus));
    }

    /**
     * Listar pedidos recentes
     */
    @GetMapping("/recentes")
    public ResponseEntity<List<PedidoResponseDTO>> listarRecentes() {
        return ResponseEntity.ok(pedidoService.listarUltimosPedidos());
    }

    /**
     * Listar pedidos por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(pedidoService.buscarPorPeriodo(inicio, fim));
    }
}