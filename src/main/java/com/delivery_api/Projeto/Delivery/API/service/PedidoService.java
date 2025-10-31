package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.repository.PedidoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Pedido criar(Pedido pedido) {
        validarDadosPedido(pedido);
        calcularTotal(pedido);
        pedido.setStatus(PedidoStatus.PENDENTE);
        pedido.setNumeroPedido("PED-" + System.currentTimeMillis());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setDataCriacao(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorStatus(PedidoStatus status) {
        return pedidoRepository.findByStatus(status);
    }

    public Pedido alterarStatus(Long id, PedidoStatus novoStatus) {
        Pedido p = buscarPorId(id);
        validarMudancaStatus(p.getStatus(), novoStatus);
        p.alterarStatus(novoStatus);
        return pedidoRepository.save(p);
    }

    public Pedido atualizar(Long id, Pedido atualizado) {
        Pedido p = buscarPorId(id);
        if (p.getStatus() == PedidoStatus.ENTREGUE || p.getStatus() == PedidoStatus.CANCELADO) {
            throw new IllegalArgumentException("Não é possível atualizar pedidos entregues ou cancelados");
        }

        if (atualizado.getObservacoes() != null) {
            p.setObservacoes(atualizado.getObservacoes());
        }
        if (atualizado.getEnderecoEntrega() != null) {
            p.setEnderecoEntrega(atualizado.getEnderecoEntrega());
        }
        return pedidoRepository.save(p);
    }

    private void validarDadosPedido(Pedido pedido) {
        if (pedido.getProdutos() == null || pedido.getProdutos().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter pelo menos um produto");
        }

        if (pedido.getCliente() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        if (!pedido.getCliente().getAtivo()) {
            throw new IllegalArgumentException("Cliente está inativo");
        }

        if (pedido.getRestaurante() == null) {
            throw new IllegalArgumentException("Restaurante é obrigatório");
        }

        if (!pedido.getRestaurante().isAtivo()) {
            throw new IllegalArgumentException("Restaurante do pedido está inativo");
        }

        if (pedido.getEnderecoEntrega() == null || pedido.getEnderecoEntrega().trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço de entrega é obrigatório");
        }



        // Validar se todos produtos são do mesmo restaurante
        Long restauranteId = null;
        for (Produto produto : pedido.getProdutos()) {
            Produto existente = produtoRepository.findById(produto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + produto.getId()));

            if (restauranteId == null) {
                restauranteId = existente.getRestaurante().getId();
            } else if (!restauranteId.equals(existente.getRestaurante().getId())) {
                throw new IllegalArgumentException("Todos os produtos devem ser do mesmo restaurante");
            }
        }

        for (Produto produto : pedido.getProdutos()) {
            Produto existente = produtoRepository.findById(produto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + produto.getId()));

            if (!existente.isDisponivel()) {
                throw new IllegalArgumentException("Produto indisponível: " + existente.getNome());
            }
        }
    }

    private void calcularTotal(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;
        for (Produto produto : pedido.getProdutos()) {
            Produto existente = produtoRepository.findById(produto.getId()).orElse(null);
            if (existente != null) {
                total = total.add(existente.getPreco());
            }
            if (pedido.getRestaurante() != null &&
                    pedido.getRestaurante().getTaxaEntrega() != null) {
                total = total.add(pedido.getRestaurante().getTaxaEntrega());
            }
        }
        pedido.setTotal(total);
    }

    private void validarMudancaStatus(PedidoStatus atual, PedidoStatus novo) {
        if (atual == novo) {
            throw new IllegalArgumentException("O status já é " + atual);
        }

        switch (atual) {
            case PENDENTE:
                if (novo != PedidoStatus.CONFIRMADO && novo != PedidoStatus.CANCELADO) {
                    throw new IllegalArgumentException("De PENDENTE, só pode ir para CONFIRMADO ou CANCELADO");
                }
                break;
            case CONFIRMADO:
                if (novo != PedidoStatus.PREPARANDO && novo != PedidoStatus.CANCELADO) {
                    throw new IllegalArgumentException("De CONFIRMADO, só pode ir para PREPARANDO ou CANCELADO");
                }
                break;
            case PREPARANDO:
                if (novo != PedidoStatus.SAIU_PARA_ENTREGA && novo != PedidoStatus.CANCELADO) {
                    throw new IllegalArgumentException("De PREPARANDO, só pode ir para SAIU_PARA_ENTREGA ou CANCELADO");
                }
                break;
            case SAIU_PARA_ENTREGA:
                if (novo != PedidoStatus.ENTREGUE) {
                    throw new IllegalArgumentException("De SAIU_PARA_ENTREGA, só pode ir para ENTREGUE");
                }
                break;
            case ENTREGUE:
                throw new IllegalArgumentException("Pedido ENTREGUE não pode ter status alterado");
            case CANCELADO:
                throw new IllegalArgumentException("Pedido CANCELADO não pode ter status alterado");
            default:
                throw new IllegalArgumentException("Transição de status inválida");
        }
    }
}