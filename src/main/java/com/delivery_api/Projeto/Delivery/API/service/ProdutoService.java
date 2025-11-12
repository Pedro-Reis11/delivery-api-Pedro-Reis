package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ProdutoResponseDTO;
import java.util.List;

public interface ProdutoService {

    ProdutoResponseDTO cadastrar(ProdutoRequestDTO requestDTO);

    List<ProdutoResponseDTO> buscarPorCategoria(String categoria);

    List<ProdutoResponseDTO> listarPorRestaurante(Long restauranteId);

    List<ProdutoResponseDTO> listarDisponiveisPorRestaurante(Long restauranteId);

    ProdutoResponseDTO buscarPorId(Long id);

    ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO);

    ProdutoResponseDTO ativarDesativar(Long id);

}
