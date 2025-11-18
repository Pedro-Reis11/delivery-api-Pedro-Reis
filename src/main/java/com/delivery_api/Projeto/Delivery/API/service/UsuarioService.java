package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTO.request.LoginRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.UsuarioRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.LoginResponseDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.UsuarioResponseDTO;

public interface UsuarioService {

    UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);
}
