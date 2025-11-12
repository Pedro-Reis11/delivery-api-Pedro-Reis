package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ClienteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {

    ClienteResponseDTO cadastrar(ClienteRequestDTO requestDTO);

    ClienteResponseDTO buscarPorId(Long id);

    ClienteResponseDTO buscarPorEmail(String email);

    List<ClienteResponseDTO> listarAtivos();

    ClienteResponseDTO atualizar(Long id, ClienteRequestDTO requestDTO);

    ClienteResponseDTO ativarDesativar(Long id);

    List<ClienteResponseDTO> buscarPorNome(String nome);
}


