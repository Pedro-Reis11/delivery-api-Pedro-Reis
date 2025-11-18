package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTO.request.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.projection.RelatorioVendas;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteService {

    RestauranteResponseDTO cadastrar(RestauranteRequestDTO requestDTO);

    List<RestauranteResponseDTO> listarAtivos();

    RestauranteResponseDTO buscarPorId(Long id);

    List<RestauranteResponseDTO> buscarPorCategoria(String categoria);

    RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO requestDTO);

    RestauranteResponseDTO ativarDesativar(Long id);

    List<RestauranteResponseDTO> buscarPorTaxaEntrega(BigDecimal taxa);

    List<RestauranteResponseDTO> buscarTop5PorNomeAsc();

    List<RelatorioVendas> relatorioVendasPorRestaurante();

    RestauranteResponseDTO buscarPorNome(String nome);

    List<RestauranteResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo);
}
