package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.ProdutoService;

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
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Gerenciamento de produtos no sistema")
public class ProdutoController {

    private final ProdutoService produtoService;

    // ==================================================
    // CADASTRAR PRODUTO
    // ==================================================
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANTE')")
    @Operation(
            summary = "Cadastrar produto",
            description = "Cria um novo produto no sistema"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Produto já existe"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.cadastrar(dto));
    }

    // ==================================================
    // BUSCAR POR ID
    // ==================================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANTE','CLIENTE')")
    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os detalhes de um produto pelo ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    // ==================================================
    // ATUALIZAR PRODUTO
    // ==================================================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANTE')")
    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO dto) {

        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    // ==================================================
    // ATIVAR / DESATIVAR
    // ==================================================
    @PatchMapping("/{id}/ativar-desativar")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANTE')")
    @Operation(
            summary = "Ativar ou desativar produto",
            description = "Altera o status ativo/inativo de um produto"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> ativarDesativarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.ativarDesativarProduto(id));
    }

    // ==================================================
    // BUSCAR POR NOME
    // ==================================================
    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar produto por nome",
            description = "Retorna um produto pelo nome exato"
    )
    public ResponseEntity<ProdutoResponseDTO> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    // ==================================================
    // BUSCAR POR RESTAURANTE
    // ==================================================
    @GetMapping("/restaurante/{restauranteId}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Listar produtos por restaurante",
            description = "Retorna todos os produtos cadastrados para determinado restaurante"
    )
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(produtoService.buscarPorRestaurante(restauranteId));
    }

    // ==================================================
    // BUSCAR POR CATEGORIA
    // ==================================================
    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar produtos por categoria",
            description = "Retorna produtos pertencentes a uma categoria"
    )
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoria));
    }

    // ==================================================
    // BUSCAR POR FAIXA DE PREÇO
    // ==================================================
    @GetMapping("/preco")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar produtos por faixa de preço",
            description = "Lista produtos com preço dentro do intervalo informado"
    )
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorPreco(
            @RequestParam BigDecimal precoMinimo,
            @RequestParam BigDecimal precoMaximo) {

        return ResponseEntity.ok(produtoService.buscarPorPreco(precoMinimo, precoMaximo));
    }

    // ==================================================
    // LISTAR TODOS
    // ==================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(summary = "Listar todos os produtos")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodosProdutos() {
        return ResponseEntity.ok(produtoService.buscarTodosProdutos());
    }

    // ==================================================
    // PREÇO <= X
    // ==================================================
    @GetMapping("/preco/{valor}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','RESTAURANTE')")
    @Operation(
            summary = "Buscar produtos por preço máximo",
            description = "Lista produtos com preço menor ou igual ao valor fornecido"
    )
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorPrecoMenorOuIgual(@PathVariable BigDecimal valor) {
        return ResponseEntity.ok(produtoService.buscarPorPrecoMenorOuIgual(valor));
    }
}
