package com.delivery_api.Projeto.Delivery.API.DTO;

import com.delivery_api.Projeto.Delivery.API.entity.*;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {
    public ClienteResponseDTO toClienteResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setEndereco(cliente.getEndereco());
        dto.setAtivo(cliente.getAtivo());
        return dto;
    }

    public Cliente toCliente(ClienteResponseDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(dto.getEndereco());
        return cliente;
    }

    // ===== RESTAURANTE MAPPERS =====

    public RestauranteResponseDTO toRestauranteResponseDTO(Restaurante restaurante) {
        RestauranteResponseDTO dto = new RestauranteResponseDTO();
        dto.setId(restaurante.getId());
        dto.setNome(restaurante.getNome());
        dto.setCategoria(restaurante.getCategoria());
        dto.setEndereco(restaurante.getEndereco());
        dto.setTelefone(restaurante.getTelefone());
        dto.setTaxaEntrega(restaurante.getTaxaEntrega());
        dto.setAvaliacao(restaurante.getAvaliacao());
        dto.setAtivo(restaurante.getAtivo());
        dto.setDataCadastro(restaurante.getDataCadastro());
        return dto;
    }

    public Restaurante toRestaurante(RestauranteResponseDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());
        restaurante.setAvaliacao(dto.getAvaliacao());
        return restaurante;
    }

    // ===== PRODUTO MAPPERS =====

    public ProdutoResponseDTO toProdutoResponseDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setCategoria(produto.getCategoria());
        dto.setDisponivel(produto.isDisponivel());

        if (produto.getRestaurante() != null) {
            dto.setRestauranteNome(produto.getRestaurante().getNome());
            dto.setRestauranteId(produto.getRestaurante().getId());
        }

        return dto;
    }

    public Produto toProduto(ProdutoResponseDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setCategoria(dto.getCategoria());
        return produto;
    }

    // ===== PEDIDO MAPPERS =====

    public PedidoResponseDTO toPedidoResumoDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setStatus(pedido.getStatus());
        dto.setTotal(pedido.getTotal());

        if (pedido.getCliente() != null) {
            dto.setClienteNome(pedido.getCliente().getNome());
        }

        if (pedido.getRestaurante() != null) {
            dto.setRestauranteNome(pedido.getRestaurante().getNome());
        }

        return dto;
    }

    public PedidoResponseDTO toPedidoResponseDTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setStatus(pedido.getStatus());
        dto.setTotal(pedido.getTotal());
        dto.setObservacoes(pedido.getObservacoes());
        dto.setEnderecoEntrega(pedido.getEnderecoEntrega());

        // Dados do cliente
        if (pedido.getCliente() != null) {
            dto.setClienteId(pedido.getCliente().getId());
            dto.setClienteNome(pedido.getCliente().getNome());
            dto.setClienteEmail(pedido.getCliente().getEmail());
            dto.setClienteTelefone(pedido.getCliente().getTelefone());
        }

        // Dados do restaurante
        if (pedido.getRestaurante() != null) {
            dto.setRestauranteId(pedido.getRestaurante().getId());
            dto.setRestauranteNome(pedido.getRestaurante().getNome());
            dto.setRestauranteCategoria(pedido.getRestaurante().getCategoria());
            dto.setRestauranteTaxaEntrega(pedido.getRestaurante().getTaxaEntrega());
        }

        return dto;
    }

    // ===== ITEM PEDIDO MAPPER =====

    public ItemPedidoResponseDTO toItemPedidoResponseDTO(ItemPedido item) {
        ItemPedidoResponseDTO dto = new ItemPedidoResponseDTO();
        dto.setId(item.getId());
        dto.setQuantidade(item.getQuantidade());
        dto.setPrecoUnitario(item.getPrecoUnitario());
        dto.setSubtotal(item.getSubtotal());
        dto.setObservacoes(item.getObservacoes());

        if (item.getProduto() != null) {
            dto.setProdutoId(item.getProduto().getId());
            dto.setProdutoNome(item.getProduto().getNome());
        }
        return dto;
    }
}
