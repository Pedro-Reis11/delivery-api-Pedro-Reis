package com.delivery_api.Projeto.Delivery.API.controller;

import com.delivery_api.Projeto.Delivery.API.DTO.request.LoginRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.UsuarioRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.LoginResponseDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.UsuarioResponseDTO;
import com.delivery_api.Projeto.Delivery.API.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrar (@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO cadastroResponse = usuarioService.cadastrar(dto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(cadastroResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO loginResponse = usuarioService.login(dto);
        return ResponseEntity.ok(loginResponse);

    }
}
