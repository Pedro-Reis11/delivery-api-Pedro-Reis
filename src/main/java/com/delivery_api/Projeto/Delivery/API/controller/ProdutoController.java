package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Cadastrar novo produto por restaurante
     */
    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<?> cadastrar(@PathVariable Long restauranteId,
                                       @Validated @RequestBody Produto produto) {
        try {
            Produto produtoSalvo = produtoService.cadastrarPorRestaurante(restauranteId, produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Listar todos os produtos (ou por restaurante via query param)
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listar(@RequestParam(required = false) Long restauranteId) {
        List<Produto> produtos;
        if (restauranteId != null) {
            produtos = produtoService.listarPorRestaurante(restauranteId);
        } else {
            produtos = produtoService.listarPorRestaurante(null);
        }
        return ResponseEntity.ok(produtos);
    }

    /**
     * Buscar produto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Produto produto = produtoService.buscarPorId(id);
            return ResponseEntity.ok(produto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Atualizar produto
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Validated @RequestBody Produto produto) {
        try {
            Produto produtoAtualizado = produtoService.atualizar(id, produto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Desativar produto (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> desativar(@PathVariable Long id) {
        try {
            produtoService.inativar(id);
            return ResponseEntity.ok().body("Produto desativado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    /**
     * Buscar produtos disponíveis por restaurante
     */
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<List<Produto>> buscarDisponiveisPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.listarDisponiveisPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }
}