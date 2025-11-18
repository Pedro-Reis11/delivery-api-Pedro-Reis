package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.ProdutoService;
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
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @Operation(summary = "Cadastrar produto",
            description = "Cria um novo produto no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Produto já existe")
    })
    public ResponseEntity<ProdutoResponseDTO> cadastrar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO produto = produtoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID",
            description = "Recupera os detalhes de um produto específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto",
            description = "Atualiza os detalhes de um produto existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO produtoAtualizado = produtoService.atualizar(id, dto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @PatchMapping("/{id}/ativar-desativar")
    @Operation(summary = "Ativar/Desativar produto",
            description = "Ativa ou desativa um produto pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto ativado/desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> ativarDesativarProduto(@PathVariable Long id) {
        ProdutoResponseDTO produtoAtualizado = produtoService.ativarDesativarProduto(id);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar produto por nome",
            description = "Recupera os detalhes de um produto específico pelo nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorNome(@PathVariable String nome) {
        ProdutoResponseDTO produto = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Buscar produtos por restaurante",
            description = "Lista todos os produtos de um restaurante específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorRestaurante(restauranteId);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar produtos por categoria",
            description = "Lista todos os produtos de uma categoria específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/preco")
    @Operation(summary = "Buscar produtos por faixa de preço",
            description = "Lista todos os produtos dentro de uma faixa de preço específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado na faixa de preço")
    })
    public ResponseEntity<List<ProdutoResponseDTO>>buscarPorPreco(@RequestParam BigDecimal precoMinimo, @RequestParam BigDecimal precoMaximo) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorPreco(precoMinimo, precoMaximo);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos",
            description = "Lista todos os produtos disponíveis no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de produtos recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarTodosProdutos() {
        List<ProdutoResponseDTO> produtos = produtoService.buscarTodosProdutos();
        return ResponseEntity.ok(produtos);
    }
    // preço menor ou igual a 20.00
    @GetMapping("/preco/{valor}")
    @Operation(summary = "Buscar produtos por preço menor ou igual",
            description = "Lista todos os produtos com preço menor ou igual ao valor especificado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado com preço menor ou igual ao valor especificado")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorPrecoMenorOuIgual(@PathVariable BigDecimal valor) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorPrecoMenorOuIgual(valor);
        return ResponseEntity.ok(produtos);
    }
}
