package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurentes")
@CrossOrigin(origins = "*")
public class RestauranteController {
    @Autowired
    private RestauranteService restauranteService;

    //Cadastrar restaurante//
    @PostMapping
    public ResponseEntity<?> criar(@Validated @RequestBody Restaurante restaurante) {
        try {
            Restaurante novoRestaurante = restauranteService.cadastrar(restaurante);
            return ResponseEntity.ok(novoRestaurante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /**
     * Listar todos os restaurantes ativos
     */
    @GetMapping
    public ResponseEntity<List<Restaurante>> listar() {
        List<Restaurante> restaurantes = restauranteService.listarAtivos();
        return ResponseEntity.ok(restaurantes);
    }

    /**
     * Buscar restaurante por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Restaurante restaurante = restauranteService.buscarPorId(id);
            return ResponseEntity.ok(restaurante);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Restaurante não encontrado");
        }
    }

    /**
     * Atualizar restaurante
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Validated @RequestBody Restaurante restaurante) {
        try {
            Restaurante atualizado = restauranteService.atualizar(id, restaurante);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /**
     * Inativar restaurante (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            restauranteService.inativar(id);
            return ResponseEntity.ok("Restaurante inativado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    /**
     * Buscar restaurantes por categoria
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorCategoria(@RequestParam String categoria) {
        try {
            List<Restaurante> restaurantes = restauranteService.buscarPorCategoria(categoria);
            if (restaurantes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhum restaurante encontrado para a categoria: " + categoria);
            }
            return ResponseEntity.ok(restaurantes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

}
