package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.ClienteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.ClienteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Cadastrar novo cliente
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO response = clienteService.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar todos os clientes ativos
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        List<ClienteResponseDTO> clientes = clienteService.listarAtivos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Atualizar cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO) {
        ClienteResponseDTO clienteAtualizado = clienteService.atualizar(id, requestDTO);
        return ResponseEntity.ok(clienteAtualizado);
    }

    /**
     * Inativar cliente (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.ok("Cliente inativado com sucesso");
    }

    /**
     * Buscar clientes por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ClienteResponseDTO> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Buscar cliente por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponseDTO> buscarPorEmail(@PathVariable String email) {
        ClienteResponseDTO cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }
}