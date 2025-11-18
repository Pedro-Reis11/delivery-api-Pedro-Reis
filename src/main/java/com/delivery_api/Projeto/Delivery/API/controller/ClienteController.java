package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ClienteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ClienteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.ClienteService;
import com.delivery_api.Projeto.Delivery.API.security.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Gerenciamento de clientes do sistema de delivery")
@SecurityRequirement(name = "bearerAuth") // habilita o cadeado no Swagger
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private SecurityUtils securityUtils;

    // ------------------------------------------------------------------------
    // CADASTRAR CLIENTE (ENDPOINT PÚBLICO)
    // ------------------------------------------------------------------------
    @PostMapping
    @Operation(
            summary = "Cadastrar cliente",
            description = "Cria um novo cliente no sistema. Endpoint público."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Cliente cadastrado com sucesso",
            content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
    )
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @Valid @RequestBody(description = "Dados do cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class)))
            ClienteRequestDTO dto) {

        ClienteResponseDTO cliente = clienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // ------------------------------------------------------------------------
    // BUSCAR POR ID (CLIENTE LOGADO OU ADMIN)
    // ------------------------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID",
            description = "Recupera os detalhes de um cliente específico. Protegido.")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PreAuthorize("@securityUtils.isOwnerCliente(#id) or hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    // ------------------------------------------------------------------------
    // BUSCAR POR EMAIL (CLIENTE LOGADO OU ADMIN)
    // ------------------------------------------------------------------------
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por email", description = "Protegido.")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PreAuthorize("@securityUtils.isOwnerEmail(#email) or hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
        ClienteResponseDTO cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }

    // ------------------------------------------------------------------------
    // LISTAR CLIENTES ATIVOS (ADMIN)
    // ------------------------------------------------------------------------
    @GetMapping
    @Operation(summary = "Listar clientes ativos",
            description = "Lista todos os clientes ativos. Apenas ADMIN pode acessar.")
    @ApiResponse(responseCode = "200",
            description = "Lista recuperada",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClienteResponseDTO.class))))
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDTO>> listarAtivos() {
        List<ClienteResponseDTO> clientes = clienteService.listarAtivos();
        return ResponseEntity.ok(clientes);
    }

    // ------------------------------------------------------------------------
    // ATUALIZAR CLIENTE (PRÓPRIO CLIENTE OU ADMIN)
    // ------------------------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados do cliente.")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PreAuthorize("@securityUtils.isOwnerCliente(#id) or hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody(description = "Dados do cliente",
                    content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class)))
            ClienteRequestDTO dto) {

        ClienteResponseDTO clienteAtualizado = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(clienteAtualizado);
    }

    // ------------------------------------------------------------------------
    // ATIVAR / DESATIVAR CLIENTE (ADMIN)
    // ------------------------------------------------------------------------
    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/Desativar cliente",
            description = "Altera o status do cliente. Apenas ADMIN.")
    @ApiResponse(responseCode = "200", description = "Status alterado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> ativarDesativarCliente(@PathVariable Long id) {
        ClienteResponseDTO clienteAtualizado = clienteService.ativarDesativar(id);
        return ResponseEntity.ok(clienteAtualizado);
    }

    // ------------------------------------------------------------------------
    // BUSCAR POR NOME (ENDPOINT PÚBLICO)
    // ------------------------------------------------------------------------
    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nome",
            description = "Retorna clientes cujo nome contém o termo. Endpoint público.")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }
}
