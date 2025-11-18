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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private RestauranteRepository restauranteRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ModelMapper modelMapper;

    // ============================================================
    // CRIAR PEDIDO
    // ============================================================
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
            throw new BusinessException("O pedido deve conter ao menos 1 item");
        }

        if (requestDTO.getEnderecoEntrega() == null || requestDTO.getEnderecoEntrega().isBlank()) {
            throw new BusinessException("Endereço de entrega é obrigatório");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(requestDTO.getEnderecoEntrega());
        pedido.setObservacoes(requestDTO.getObservacoes());
        pedido.setStatus(PedidoStatus.PENDENTE);
        pedido.setNumeroPedido("PED-" + System.currentTimeMillis());
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setDataPedido(LocalDateTime.now());

        // valida todos os itens
        Set<Long> idsRestauranteItens = new HashSet<>();

        for (ItemPedidoRequestDTO itemReq : requestDTO.getItens()) {

            Produto produto = produtoRepository.findById(itemReq.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto", itemReq.getProdutoId()));

            if (!produto.getDisponivel()) {
                throw new BusinessException("Produto indisponível: " + produto.getNome());
            }

            idsRestauranteItens.add(produto.getRestaurante().getId());

            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new BusinessException("O produto " + produto.getNome() + " não pertence ao restaurante escolhido");
            }

            if (itemReq.getQuantidade() == null || itemReq.getQuantidade() <= 0) {
                throw new BusinessException("Quantidade inválida para o produto " + produto.getNome());
            }

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemReq.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setObservacoes(itemReq.getObservacoes());
            item.calcularSubtotal();

            pedido.adicionarItem(item);
        }

        if (idsRestauranteItens.size() > 1) {
            throw new BusinessException("Todos os produtos devem ser do mesmo restaurante");
        }

        pedido.calcularTotal();

        Pedido salvo = pedidoRepository.save(pedido);
        return modelMapper.map(salvo, PedidoResponseDTO.class);
    }

    // ============================================================
    // LISTAR
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .toList();
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

        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

        if (pedidos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pedido encontrado para o cliente ID: " + clienteId);
        }

        return pedidos.stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorStatus(PedidoStatus status) {
        return pedidoRepository.findByStatus(status)
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .toList();
    }

    // ============================================================
    // ALTERAR STATUS
    // ============================================================
    @Override
    public PedidoResponseDTO alterarStatus(Long id, PedidoStatus novo) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido", id));

        validarTransicaoStatus(pedido.getStatus(), novo);

        pedido.setStatus(novo);

        Pedido atualizado = pedidoRepository.save(pedido);
        return modelMapper.map(atualizado, PedidoResponseDTO.class);
    }

    private void validarTransicaoStatus(PedidoStatus atual, PedidoStatus novo) {

        if (atual == PedidoStatus.CANCELADO || atual == PedidoStatus.ENTREGUE) {
            throw new BusinessException("Pedidos ENTREGUE ou CANCELADO não podem mudar de status");
        }

        boolean valido =
                (atual == PedidoStatus.PENDENTE && (novo == PedidoStatus.CONFIRMADO || novo == PedidoStatus.CANCELADO)) ||
                        (atual == PedidoStatus.CONFIRMADO && (novo == PedidoStatus.PREPARANDO || novo == PedidoStatus.CANCELADO)) ||
                        (atual == PedidoStatus.PREPARANDO && novo == PedidoStatus.SAIU_PARA_ENTREGA) ||
                        (atual == PedidoStatus.SAIU_PARA_ENTREGA && novo == PedidoStatus.ENTREGUE);

        if (!valido) {
            throw new BusinessException("Transição de status inválida: " + atual + " → " + novo);
        }
    }

    // ============================================================
    // ATUALIZAR
    // ============================================================
    @Override
    public PedidoResponseDTO atualizar(Long id, PedidoRequestDTO dto) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido", id));

        if (pedido.getStatus() == PedidoStatus.ENTREGUE || pedido.getStatus() == PedidoStatus.CANCELADO) {
            throw new BusinessException("Pedidos ENTREGUE ou CANCELADO não podem ser atualizados");
        }

        if (dto.getObservacoes() != null) pedido.setObservacoes(dto.getObservacoes());
        if (dto.getEnderecoEntrega() != null) pedido.setEnderecoEntrega(dto.getEnderecoEntrega());

        Pedido atualizado = pedidoRepository.save(pedido);
        return modelMapper.map(atualizado, PedidoResponseDTO.class);
    }

    // ============================================================
    // CANCELAR PEDIDO
    // ============================================================
    @Override
    public PedidoResponseDTO cancelarPedido(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido", id));

        if (!podeSerCancelado(pedido.getStatus())) {
            throw new BusinessException("Pedido não pode ser cancelado no status atual: " + pedido.getStatus());
        }

        pedido.setStatus(PedidoStatus.CANCELADO);

        Pedido salvo = pedidoRepository.save(pedido);
        return modelMapper.map(salvo, PedidoResponseDTO.class);
    }

    private boolean podeSerCancelado(PedidoStatus status) {
        return status == PedidoStatus.PENDENTE || status == PedidoStatus.CONFIRMADO;
    }

    // ============================================================
    // CONSULTAS
    // ============================================================
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarUltimosPedidos() {
        return pedidoRepository.findTop10ByOrderByDataPedidoDesc()
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim)
                .stream()
                .map(p -> modelMapper.map(p, PedidoResponseDTO.class))
                .toList();
    }

    // ============================================================
    // CALCULAR TOTAL
    // ============================================================
    @Override
    public BigDecimal calcularValorTotalPedido(List<ItemPedidoRequestDTO> itens) {

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO item : itens) {

            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto", item.getProdutoId()));

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new BusinessException("Quantidade inválida para o produto: " + produto.getNome());
            }

            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        return total;
    }
}
