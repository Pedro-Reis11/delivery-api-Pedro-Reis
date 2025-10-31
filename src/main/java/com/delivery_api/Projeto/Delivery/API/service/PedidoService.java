package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTOs.ItemPedidoRequest;
import com.delivery_api.Projeto.Delivery.API.DTOs.PedidoRequest;
import com.delivery_api.Projeto.Delivery.API.entity.*;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import com.delivery_api.Projeto.Delivery.API.repository.ClienteRepository;
import com.delivery_api.Projeto.Delivery.API.repository.PedidoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
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
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Pedido criar(PedidoRequest request) {
        // Buscar cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + request.getClienteId()));

        if (!cliente.getAtivo()) {
            throw new IllegalArgumentException("Cliente está inativo");
        }

        // Buscar restaurante
        Restaurante restaurante = restauranteRepository.findById(request.getRestauranteId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + request.getRestauranteId()));

        if (!restaurante.isAtivo()) {
            throw new IllegalArgumentException("Restaurante está inativo");
        }

        // Validar itens
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter pelo menos um item");
        }

        // Validar endereço
        if (request.getEnderecoEntrega() == null || request.getEnderecoEntrega().trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço de entrega é obrigatório");
        }

        // Criar pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(request.getEnderecoEntrega());
        pedido.setObservacoes(request.getObservacoes());
        pedido.setStatus(PedidoStatus.PENDENTE);
        pedido.setNumeroPedido("PED-" + System.currentTimeMillis());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setDataCriacao(LocalDateTime.now());

        // Validar e adicionar itens com quantidade
        Set<Long> restaurantesDosProdutos = new HashSet<>();

        for (ItemPedidoRequest itemRequest : request.getItens()) {
            // Validar quantidade
            if (itemRequest.getQuantidade() == null || itemRequest.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }

            // Buscar produto
            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemRequest.getProdutoId()));

            if (!produto.isDisponivel()) {
                throw new IllegalArgumentException("Produto indisponível: " + produto.getNome());
            }

            // Validar se produto pertence ao restaurante do pedido
            restaurantesDosProdutos.add(produto.getRestaurante().getId());
            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new IllegalArgumentException("Produto " + produto.getNome() + " não pertence ao restaurante selecionado");
            }

            // Criar item do pedido
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemRequest.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());  // Salva preço atual
            item.setObservacoes(itemRequest.getObservacoes());
            item.calcularSubtotal();  // quantidade * preço

            pedido.adicionarItem(item);
        }

        // Verificar se há produtos de múltiplos restaurantes
        if (restaurantesDosProdutos.size() > 1) {
            throw new IllegalArgumentException("Todos os produtos devem ser do mesmo restaurante");
        }

        // Calcular total com base nos itens e taxa de entrega
        pedido.calcularTotal();

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