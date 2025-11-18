package com.delivery_api.Projeto.Delivery.API.service.impl;

import com.delivery_api.Projeto.Delivery.API.DTO.request.LoginRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.UsuarioRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.LoginResponseDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.UsuarioResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Usuario;
import com.delivery_api.Projeto.Delivery.API.enums.Role;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.repository.UsuarioRepository;
import com.delivery_api.Projeto.Delivery.API.security.JwtUtil;
import com.delivery_api.Projeto.Delivery.API.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + dto.getEmail());
        }

        Role role;
        try {
            role = Role.valueOf(dto.getRole().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("Role inválida: " + dto.getRole());
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(role)
                .ativo(true)
                .dataCriacao(LocalDateTime.now())
                .restauranteId(dto.getRestauranteId())
                .build();

        usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas"));

        String token = jwtUtil.generateToken(usuario, usuario);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUsuario(modelMapper.map(usuario, UsuarioResponseDTO.class));
        response.setToken(token);
        response.setTipo("Bearer");
        response.setExpiracao(jwtUtil.extractExpiration(token).getTime());

        return response;
    }
}
