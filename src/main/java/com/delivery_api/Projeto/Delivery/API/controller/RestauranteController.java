package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.projection.RelatorioVendas;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    /**
     * Cadastrar restaurante
     */
    @PostMapping
    @Operation(summary = "Cadastrar restaurante", description = "Cadastra um novo restaurante na plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody RestauranteRequestDTO dto) {
        RestauranteResponseDTO response = restauranteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar restaurantes ativos
     */
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(restauranteService.listarAtivos());
    }

    /**
     * Buscar restaurante por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    /**
     * Buscar por categoria
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }

    /**
     * Buscar por taxa de entrega
     */
    @GetMapping("/taxa/{taxa}")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorTaxaEntrega(@PathVariable BigDecimal taxa) {
        return ResponseEntity.ok(restauranteService.buscarPorTaxaEntregaMenorOuIgual(taxa));
    }

    /**
     * Buscar top 5 restaurantes
     */
    @GetMapping("/top5")
    public ResponseEntity<List<RestauranteResponseDTO>> buscarTop5() {
        return ResponseEntity.ok(restauranteService.buscarTop5PorNomeAsc());
    }

    /**
     * Relatório de vendas
     */
    @GetMapping("/relatorio-vendas")
    public ResponseEntity<List<RelatorioVendas>> relatorioVendas() {
        return ResponseEntity.ok(restauranteService.relatorioVendasPorRestaurante());
    }

    /**
     * Atualizar restaurante
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody RestauranteRequestDTO dto) {
        return ResponseEntity.ok(restauranteService.atualizar(id, dto));
    }

    /**
     * Ativar ou desativar restaurante
     */
    @PatchMapping("/{id}/ativar-desativar")
    public ResponseEntity<RestauranteResponseDTO> ativarDesativar(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.ativarDesativar(id));
    }
}
