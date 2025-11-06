package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO response = pedidoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar todos os pedidos (ou por cliente via query param)
     */
    @GetMapping
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
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Atualizar status do pedido
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusRequest request) {
        PedidoResponseDTO pedidoAtualizado = pedidoService.alterarStatus(id, request.status());
        return ResponseEntity.ok(pedidoAtualizado);
    }

    /**
     * Atualizar informações do pedido (observações e endereço)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO requestDTO) {
        PedidoResponseDTO pedidoAtualizado = pedidoService.atualizar(id, requestDTO);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    /**
     * Listar pedidos por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorStatus(@PathVariable PedidoStatus status) {
        List<PedidoResponseDTO> pedidos = pedidoService.listarPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }
}