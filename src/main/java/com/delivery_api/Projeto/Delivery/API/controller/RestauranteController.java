package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ApiResponseWrapper;
import com.delivery_api.Projeto.Delivery.API.DTO.response.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.projection.RelatorioVendas;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Gerenciamento de restaurantes no sistema")
public class RestauranteController {

    private final RestauranteService restauranteService;

    // ==================================================
    // CADASTRAR
    // ==================================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Cadastrar restaurante",
            description = "Cria um novo restaurante no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Restaurante já cadastrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<RestauranteResponseDTO> cadastrar(
            @Valid @RequestBody RestauranteRequestDTO dto) {

        RestauranteResponseDTO restaurante = restauranteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }

    // ==================================================
    // LISTAR TODOS (ADMIN e CLIENTE)
    // ==================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    @Operation(
            summary = "Listar restaurantes",
            description = "Retorna todos os restaurantes ativos com opção de paginação"
    )
    public ResponseEntity<ApiResponseWrapper<List<RestauranteResponseDTO>>> listarTodos(Pageable pageable) {

        List<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivos();
        ApiResponseWrapper<List<RestauranteResponseDTO>> response =
                new ApiResponseWrapper<>(true, restaurantes, "Busca realizada com sucesso");

        return ResponseEntity.ok(response);
    }

    // ==================================================
    // BUSCAR POR ID
    // ==================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar restaurante por ID",
            description = "Recupera um restaurante específico pelo ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    // ==================================================
    // ATUALIZAR
    // ==================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANTE')")
    @Operation(
            summary = "Atualizar restaurante",
            description = "Atualiza os dados de um restaurante existente"
    )
    public ResponseEntity<RestauranteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RestauranteRequestDTO dto) {

        return ResponseEntity.ok(restauranteService.atualizar(id, dto));
    }

    // ==================================================
    // ATIVAR / DESATIVAR
    // ==================================================
    @PatchMapping("/{id}/ativar-desativar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Ativar/Desativar restaurante",
            description = "Altera o status ativo/inativo de um restaurante"
    )
    public ResponseEntity<RestauranteResponseDTO> ativarDesativar(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.ativarDesativar(id));
    }

    // ==================================================
    // BUSCAR POR NOME
    // ==================================================
    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar restaurante por nome",
            description = "Recupera um restaurante pelo nome"
    )
    public ResponseEntity<RestauranteResponseDTO> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(restauranteService.buscarPorNome(nome));
    }

    // ==================================================
    // BUSCAR POR PREÇO
    // ==================================================
    @GetMapping("/preco/{min}/{max}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar restaurantes por faixa de preço",
            description = "Lista restaurantes dentro de uma faixa de preço"
    )
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorPreco(
            @PathVariable BigDecimal min,
            @PathVariable BigDecimal max) {

        return ResponseEntity.ok(restauranteService.buscarPorPreco(min, max));
    }

    // ==================================================
    // BUSCAR POR CATEGORIA
    // ==================================================
    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar restaurantes por categoria",
            description = "Lista restaurantes pertencentes a uma categoria específica"
    )
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(restauranteService.buscarPorCategoria(categoria));
    }

    // ==================================================
    // INATIVAR DEFINITIVO (ADMIN)
    // ==================================================
    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Inativar restaurante",
            description = "Inativa um restaurante definitivamente"
    )
    public ResponseEntity<RestauranteResponseDTO> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(restauranteService.ativarDesativar(id));
    }

    // ==================================================
    // TAXA DE ENTREGA
    // ==================================================
    @GetMapping("/taxa-entrega")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar restaurantes por taxa de entrega",
            description = "Lista restaurantes com taxa de entrega igual ao valor informado"
    )
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorTaxa(@RequestParam BigDecimal taxa) {
        return ResponseEntity.ok(restauranteService.buscarPorTaxaEntrega(taxa));
    }

    // ==================================================
    // TOP 5
    // ==================================================
    @GetMapping("/top-cinco")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    @Operation(
            summary = "Top 5 restaurantes",
            description = "Retorna os 5 restaurantes mais populares"
    )
    public ResponseEntity<List<RestauranteResponseDTO>> top5() {
        return ResponseEntity.ok(restauranteService.buscarTop5PorNomeAsc());
    }

    // ==================================================
    // RELATÓRIO DE VENDAS
    // ==================================================
    @GetMapping("/relatorio-vendas")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Relatório de vendas",
            description = "Gera relatório de vendas por restaurante"
    )
    public ResponseEntity<List<RelatorioVendas>> relatorio() {
        return ResponseEntity.ok(restauranteService.relatorioVendasPorRestaurante());
    }
}
