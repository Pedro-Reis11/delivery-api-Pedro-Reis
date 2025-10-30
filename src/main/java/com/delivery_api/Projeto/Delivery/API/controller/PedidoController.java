package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Criar novo pedido
     */
    @PostMapping
    public ResponseEntity<?> criar(@Validated @RequestBody Pedido pedido) {
        try {
            Pedido pedidoSalvo = pedidoService.criar(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Listar todos os pedidos (ou por cliente via query param)
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> listar(@RequestParam(required = false) Long clienteId) {
        List<Pedido> pedidos;
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
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Atualizar status do pedido
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id,
                                             @RequestBody PedidoStatus novoStatus) {
        try {
            Pedido pedidoAtualizado = pedidoService.alterarStatus(id, novoStatus);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
}