package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ApiResponseWrapper;
import com.delivery_api.Projeto.Delivery.API.DTO.response.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.projection.RelatorioVendas;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    @Operation(summary = "Cadastrar restaurante",
            description = "Cria um novo restaurante no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Restaurante já existe")
    })
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@Valid @RequestBody RestauranteRequestDTO dto) {
        RestauranteResponseDTO restaurante = restauranteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    }

    @GetMapping
    @Operation(summary = "Listar pedidos",
            description = "Lista pedidos com filtros opcionais e paginação")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    public ResponseEntity<ApiResponseWrapper<List<RestauranteResponseDTO>>> listarTodos(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @Parameter(description = "Parâmetros de paginação")
            Pageable pageable) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.listarAtivos();
        ApiResponseWrapper<List<RestauranteResponseDTO>> response = new ApiResponseWrapper<>(true, restaurantes, "Busca Realizada com sucesso");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID",
            description = "Recupera os detalhes de um restaurante específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@PathVariable Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(restaurante);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante",
            description = "Atualiza os detalhes de um restaurante existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteRequestDTO dto) {
        RestauranteResponseDTO restauranteAtualizado = restauranteService.atualizar(id, dto);
        return ResponseEntity.ok(restauranteAtualizado);
    }
    @PatchMapping("/{id}/ativar-desativar")
    @Operation(summary = "Ativar/Desativar restaurante",
            description = "Ativa ou desativa um restaurante pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> ativarDesativarRestaurante(@PathVariable Long id) {
        RestauranteResponseDTO restauranteAtualizado = restauranteService.ativarDesativar(id);
        return ResponseEntity.ok(restauranteAtualizado);
    }
    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar restaurante por nome",
            description = "Recupera os detalhes de um restaurante específico pelo nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> buscarPorNome(@PathVariable String nome) {
        RestauranteResponseDTO restaurante = restauranteService.buscarPorNome(nome);
        return ResponseEntity.ok(restaurante);
    }
    @GetMapping("/preco/{precoMinimo}/{precoMaximo}")
    @Operation(summary = "Buscar restaurantes por faixa de preço",
            description = "Lista todos os restaurantes dentro de uma faixa de preço específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado dentro da faixa de preço")
    })
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorPreco(@PathVariable BigDecimal precoMinimo, @PathVariable BigDecimal precoMaximo) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorPreco(precoMinimo, precoMaximo);
        return ResponseEntity.ok(restaurantes);
    }
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar restaurantes por categoria",
            description = "Lista todos os restaurantes de uma categoria específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado na categoria")
    })
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(restaurantes);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar restaurante",
            description = "Inativa um restaurante pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteResponseDTO> inativarRestaurante(@PathVariable Long id) {
        RestauranteResponseDTO restauranteInativado = restauranteService.ativarDesativar(id);
        return ResponseEntity.ok(restauranteInativado);
    }

    @GetMapping("/taxa-entrega")
    @Operation(summary = "Buscar restaurantes por taxa de entrega",
            description = "Lista todos os restaurantes com uma taxa de entrega específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurantes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado com a taxa de entrega especificada")
    })
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorTaxaEntrega(@RequestParam BigDecimal taxa) {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarPorTaxaEntrega(taxa);
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/top-cinco")
    @Operation(summary = "Listar os 5 restaurantes mais populares por nome",
            description = "Retorna os 5 restaurantes mais populares ordenados por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista dos 5 restaurantes mais populares retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum restaurante encontrado")
    })
    public ResponseEntity<List<RestauranteResponseDTO>> listarTop5PorNome() {
        List<RestauranteResponseDTO> top5Restaurantes = restauranteService.buscarTop5PorNomeAsc();
        return ResponseEntity.ok(top5Restaurantes);
    }

    @GetMapping("/relatorio-vendas")
    @Operation(summary = "Gerar relatório de vendas por restaurante",
            description = "Gera um relatório de vendas agrupado por restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relatório de vendas gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum dado de vendas encontrado")
    })
    public ResponseEntity<List<RelatorioVendas>> relatorioVendasPorRestaurante() {
        List<RelatorioVendas> relatorio = restauranteService.relatorioVendasPorRestaurante();
        return ResponseEntity.ok(relatorio);
    }
}
