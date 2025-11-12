package com.delivery_api.Projeto.Delivery.API.service.impl;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ItemPedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.request.PedidoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.PedidoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.*;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.exception.EntityNotFoundException;
import com.delivery_api.Projeto.Delivery.API.repository.*;
import com.delivery_api.Projeto.Delivery.API.service.PedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PedidoResponseDTO criar(PedidoRequestDTO requestDTO) {
        Cliente cliente = clienteRepository.findById(requestDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente", requestDTO.getClienteId()));

        if (!cliente.getAtivo()) {
            throw new BusinessException("Cliente está inativo");
        }

        Restaurante restaurante = restauranteRepository.findById(requestDTO.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", requestDTO.getRestauranteId()));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Restaurante está inativo");
        }

        if (requestDTO.getItens() == null || requestDTO.getItens().isEmpty()) {
            throw new BusinessException("Pedido deve conter pelo menos um item");
        }

        if (requestDTO.getEnderecoEntrega() == null || requestDTO.getEnderecoEntrega().trim().isEmpty()) {
            throw new BusinessException("Endereço de entrega é obrigatório");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(requestDTO.getEnderecoEntrega());
        pedido.setObservacoes(requestDTO.getObservacoes());
        pedido.setStatus(PedidoStatus.PENDENTE);
        pedido.setNumeroPedido("PED-" + System.currentTimeMillis());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setDataCriacao(LocalDateTime.now());

        Set<Long> restaurantesDosProdutos = new HashSet<>();

        for (ItemPedidoRequestDTO itemRequest : requestDTO.getItens()) {
            if (itemRequest.getQuantidade() == null || itemRequest.getQuantidade() <= 0) {
                throw new BusinessException("Quantidade deve ser maior que zero");
            }

            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto", itemRequest.getProdutoId()));

            if (!produto.isDisponivel()) {
                throw new BusinessException("Produto indisponível: " + produto.getNome());
            }

            restaurantesDosProdutos.add(produto.getRestaurante().getId());
            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new BusinessException("Produto " + produto.getNome() + " não pertence ao restaurante selecionado");
            }

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setObservacoes(itemRequest.getObservacoes());
            item.calcularSubtotal();

            pedido.adicionarItem(item);
        }

        if (restaurantesDosProdutos.size() > 1) {
            throw new BusinessException("Todos os produtos devem ser do mesmo restaurante");
        }

        pedido.calcularTotal();

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido", id));
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorStatus(PedidoStatus status) {
        return pedidoRepository.findByStatus(status)
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO alterarStatus(Long id, PedidoStatus novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido", id));

        validarMudancaStatus(pedido.getStatus(), novoStatus);
        pedido.alterarStatus(novoStatus);

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return modelMapper.map(pedidoAtualizado, PedidoResponseDTO.class);
    }

    @Override
    public PedidoResponseDTO atualizar(Long id, PedidoRequestDTO requestDTO) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido", id));

        if (pedido.getStatus() == PedidoStatus.ENTREGUE || pedido.getStatus() == PedidoStatus.CANCELADO) {
            throw new BusinessException("Não é possível atualizar pedidos entregues ou cancelados");
        }

        if (requestDTO.getObservacoes() != null) {
            pedido.setObservacoes(requestDTO.getObservacoes());
        }

        if (requestDTO.getEnderecoEntrega() != null) {
            pedido.setEnderecoEntrega(requestDTO.getEnderecoEntrega());
        }

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return modelMapper.map(pedidoAtualizado, PedidoResponseDTO.class);
    }

    private void validarMudancaStatus(PedidoStatus atual, PedidoStatus novo) {
        if (atual == novo) {
            throw new BusinessException("O status já é " + atual);
        }

        switch (atual) {
            case PENDENTE:
                if (novo != PedidoStatus.CONFIRMADO && novo != PedidoStatus.CANCELADO) {
                    throw new BusinessException("De PENDENTE, só pode ir para CONFIRMADO ou CANCELADO");
                }
                break;
            case CONFIRMADO:
                if (novo != PedidoStatus.PREPARANDO && novo != PedidoStatus.CANCELADO) {
                    throw new BusinessException("De CONFIRMADO, só pode ir para PREPARANDO ou CANCELADO");
                }
                break;
            case PREPARANDO:
                if (novo != PedidoStatus.SAIU_PARA_ENTREGA && novo != PedidoStatus.CANCELADO) {
                    throw new BusinessException("De PREPARANDO, só pode ir para SAIU_PARA_ENTREGA ou CANCELADO");
                }
                break;
            case SAIU_PARA_ENTREGA:
                if (novo != PedidoStatus.ENTREGUE) {
                    throw new BusinessException("De SAIU_PARA_ENTREGA, só pode ir para ENTREGUE");
                }
                break;
            case ENTREGUE:
                throw new BusinessException("Pedido ENTREGUE não pode ter status alterado");
            case CANCELADO:
                throw new BusinessException("Pedido CANCELADO não pode ter status alterado");
            default:
                throw new BusinessException("Transição de status inválida");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarUltimosPedidos() {
        return pedidoRepository.findTop10ByOrderByDataPedidoDesc()
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim)
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByDataPedidoDesc(clienteId)
                .stream()
                .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
                .collect(Collectors.toList());
    }
}
