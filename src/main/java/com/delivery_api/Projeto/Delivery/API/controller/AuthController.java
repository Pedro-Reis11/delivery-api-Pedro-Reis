package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.LoginRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.UsuarioRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.LoginResponseDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.UsuarioResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
public class AuthController {

    private final UsuarioService usuarioService;

    // ==============================================
    //  REGISTRO
    // ==============================================
    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário",
            description = "Cria um novo usuário com senha criptografada e papel padrão definido.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    public ResponseEntity<UsuarioResponseDTO> registrar(
            @Valid @RequestBody UsuarioRequestDTO dto
    ) {
        UsuarioResponseDTO response = usuarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==============================================
    //  LOGIN (GERAÇÃO DO JWT)
    // ==============================================
    @PostMapping("/login")
    @Operation(summary = "Login do usuário",
            description = "Valida as credenciais e gera um token JWT para acesso às APIs protegidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto
    ) {
        LoginResponseDTO response = usuarioService.login(dto);
        return ResponseEntity.ok(response);
    }
}
