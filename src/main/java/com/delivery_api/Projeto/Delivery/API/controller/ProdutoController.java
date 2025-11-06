package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Cadastrar novo produto
     */
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO requestDTO) {
        ProdutoResponseDTO response = produtoService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cadastrar produto por restaurante
     */
    @PostMapping("/restaurante/{restauranteId}")
    public ResponseEntity<ProdutoResponseDTO> cadastrarPorRestaurante(
            @PathVariable Long restauranteId,
            @Valid @RequestBody ProdutoRequestDTO requestDTO) {
        // Define o restauranteId no DTO
        requestDTO.setRestauranteId(restauranteId);
        ProdutoResponseDTO response = produtoService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar todos os produtos (ou por restaurante via query param)
     */
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar(
            @RequestParam(required = false) Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.listarPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Buscar produto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    /**
     * Atualizar produto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO requestDTO) {
        ProdutoResponseDTO produtoAtualizado = produtoService.atualizar(id, requestDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    /**
     * Desativar produto (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> desativar(@PathVariable Long id) {
        produtoService.inativar(id);
        return ResponseEntity.ok("Produto desativado com sucesso");
    }

    /**
     * Buscar produtos disponíveis por restaurante
     */
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarDisponiveisPorRestaurante(
            @PathVariable Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.listarDisponiveisPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Ativar produto
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ProdutoResponseDTO> ativar(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.ativar(id);
        return ResponseEntity.ok(produto);
    }
}