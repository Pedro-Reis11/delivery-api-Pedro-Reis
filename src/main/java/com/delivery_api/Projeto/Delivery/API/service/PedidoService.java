package com.delivery_api.Projeto.Delivery.API.service;
import com.delivery_api.Projeto.Delivery.API.DTO.request.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoService {

    PedidoResponseDTO criar(PedidoRequestDTO requestDTO);

    List<PedidoResponseDTO> listarTodos();

    PedidoResponseDTO buscarPorId(Long id);

    List<PedidoResponseDTO> listarPorCliente(Long clienteId);

    List<PedidoResponseDTO> listarPorStatus(PedidoStatus status);

    PedidoResponseDTO alterarStatus(Long id, PedidoStatus novoStatus);

    PedidoResponseDTO atualizar(Long id, PedidoRequestDTO requestDTO);

    List<PedidoResponseDTO> listarUltimosPedidos();

    List<PedidoResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);

    List<PedidoResponseDTO> buscarPorCliente(Long clienteId);
}