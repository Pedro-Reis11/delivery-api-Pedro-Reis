package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    /**
     * Cadastrar novo restaurante
     */
    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> criar(@Valid @RequestBody RestauranteRequestDTO requestDTO) {
        RestauranteResponseDTO response = restauranteService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar todos os restaurantes ativos
     */
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listar() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivos();
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Buscar restaurante por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(restaurante);
    }

    /**
     * Atualizar restaurante
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RestauranteRequestDTO requestDTO) {
        RestauranteResponseDTO restauranteAtualizado = restauranteService.atualizar(id, requestDTO);
        return ResponseEntity.ok(restauranteAtualizado);
    }

    /**
     * Inativar restaurante (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> inativar(@PathVariable Long id) {
        restauranteService.inativar(id);
        return ResponseEntity.ok("Restaurante inativado com sucesso");
    }

    /**
     * Buscar restaurantes por categoria
     */
    @GetMapping("/categoria")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@RequestParam String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorCategoria(categoria);

        if (restaurantes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(restaurantes);
        }

        return ResponseEntity.ok(restaurantes);
    }
}