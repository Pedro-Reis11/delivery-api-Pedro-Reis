package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ClienteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ClienteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cadastrar cliente",
            description = "Cria um novo cliente no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Cliente já cadastrado")
    })
    public ResponseEntity<ClienteResponseDTO> cadastrar(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO cliente = clienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID",
            description = "Recupera os detalhes de um cliente específico pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por email",
            description = "Recupera os detalhes de um cliente específico pelo email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
        ClienteResponseDTO cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    @Operation(summary = "Listar clientes ativos",
            description = "Lista todos os clientes que estão ativos no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado")
    })
    public ResponseEntity<List<ClienteResponseDTO>> listarAtivos() {
        List<ClienteResponseDTO> clientes = clienteService.listarAtivos();
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente",
            description = "Atualiza os dados de um cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO clienteAtualizado = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/Desativar cliente",
            description = "Ativa ou desativa o status de um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> ativarDesativarCliente(@PathVariable Long id) {
        ClienteResponseDTO clienteAtualizado = clienteService.ativarDesativar(id);
        return ResponseEntity.ok(clienteAtualizado);
    }
    //buscar por nome via params
    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nome",
            description = "Recupera uma lista de clientes que correspondem ao nome fornecido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado com o nome fornecido")
    })
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@Param("nome") String nome) {
        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }
}