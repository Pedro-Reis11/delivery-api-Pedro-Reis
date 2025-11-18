package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ItemPedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos no sistema")
public class PedidoController {

    private final PedidoService pedidoService;

    // ==================================================
    // CRIAR PEDIDO
    // ==================================================
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados"),
            @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO response = pedidoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================================================
    // LISTAR TODOS
    // ==================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    @Operation(summary = "Listar pedidos",
            description = "Lista todos os pedidos ou filtra por cliente usando o parâmetro clienteId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada")
    })
    public ResponseEntity<List<PedidoResponseDTO>> listar(@RequestParam(required = false) Long clienteId) {
        List<PedidoResponseDTO> pedidos =
                (clienteId != null) ? pedidoService.listarPorCliente(clienteId)
                        : pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    // ==================================================
    // BUSCAR POR ID
    // ==================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'RESTAURANTE')")
    @Operation(summary = "Buscar pedido por ID",
            description = "Retorna os detalhes de um pedido específico")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    // ==================================================
    // PEDIDOS POR CLIENTE
    // ==================================================
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @Operation(summary = "Listar pedidos por cliente",
            description = "Retorna os pedidos realizados pelo cliente especificado")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    // ==================================================
    // PEDIDOS POR STATUS
    // ==================================================
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    @Operation(summary = "Listar pedidos por status")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorStatus(@PathVariable PedidoStatus status) {
        return ResponseEntity.ok(pedidoService.listarPorStatus(status));
    }

    // ==================================================
    // ATUALIZAR PEDIDO
    // ==================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    @Operation(summary = "Atualizar pedido",
            description = "Atualiza os dados de um pedido existente")
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id,
                                                       @Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizar(id, dto));
    }

    // ==================================================
    // ALTERAR STATUS DO PEDIDO
    // ==================================================
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    @Operation(summary = "Atualizar status do pedido")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam PedidoStatus novoStatus) {
        return ResponseEntity.ok(pedidoService.alterarStatus(id, novoStatus));
    }

    // ==================================================
    // LISTAR PEDIDOS RECENTES
    // ==================================================
    @GetMapping("/recentes")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    public ResponseEntity<List<PedidoResponseDTO>> listarRecentes() {
        return ResponseEntity.ok(pedidoService.listarUltimosPedidos());
    }

    // ==================================================
    // LISTAR PEDIDOS POR PERÍODO
    // ==================================================
    @GetMapping("/periodo")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(pedidoService.buscarPorPeriodo(inicio, fim));
    }

    // ==================================================
    // CANCELAR PEDIDO
    // ==================================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }

    // ==================================================
    // CALCULAR TOTAL DO PEDIDO
    // ==================================================
    @PostMapping("/calcular")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'RESTAURANTE')")
    @Operation(summary = "Calcular valor total")
    public ResponseEntity<BigDecimal> calcularValorTotalPedido(
            @Valid @RequestBody List<ItemPedidoRequestDTO> itens) {

        return ResponseEntity.ok(pedidoService.calcularValorTotalPedido(itens));
    }
}
