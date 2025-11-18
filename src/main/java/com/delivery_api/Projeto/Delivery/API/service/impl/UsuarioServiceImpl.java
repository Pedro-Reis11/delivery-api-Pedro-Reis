package com.delivery_api.Projeto.Delivery.API.service.impl;

import com.delivery_api.Projeto.Delivery.API.DTO.request.LoginRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.UsuarioRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.LoginResponseDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.UsuarioResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Usuario;
import com.delivery_api.Projeto.Delivery.API.enums.Role;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.repository.UsuarioRepository;
import com.delivery_api.Projeto.Delivery.API.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
           throw new BusinessException("Email ja cadastrado:" + dto.getEmail());
        }
        Usuario usuario = Usuario.builder()
                .email(dto.getEmail())
                .nome(dto.getNome())
                .senha(dto.getSenha())
                .role(Role.valueOf(dto.getRole()))
                .build();
        usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais invalidas"));
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setUsuario(modelMapper.map(usuario, UsuarioResponseDTO.class));
        responseDTO.setTipo("Bearer");
        responseDTO.setExpiracao(86400000L);
        responseDTO.setToken("fake-jwt-token");
        return modelMapper.map(usuario, LoginResponseDTO.class);
    }
}
