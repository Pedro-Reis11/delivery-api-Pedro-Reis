package com.delivery_api.Projeto.Delivery.API.service;


import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    public Produto cadastrarPorRestaurante(Long restauranteId, Produto produto) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + restauranteId));

        validarDadosProduto(produto, restaurante);
        produto.setRestaurante(restaurante);
        produto.setDisponivel(true);
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Produto> listarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId);
    }

    public Produto atualizar(Long id, Produto atualizado) {
        Produto p = buscarPorId(id);
        p.setNome(atualizado.getNome());
        p.setPreco(atualizado.getPreco());
        validarPreco(p.getPreco());
        return produtoRepository.save(p);
    }

    public void inativar(Long id) {
        Produto p = buscarPorId(id);
        p.inativar();
        produtoRepository.save(p);
    }

    private void validarDadosProduto(Produto produto, Restaurante restaurante) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (produtoRepository.existsByNomeAndRestaurante(produto.getNome(), restaurante)) {
            throw new IllegalArgumentException("Produto já existe neste restaurante: " + produto.getNome());
        }
        if (produto.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome do produto deve ter pelo menos 2 caracteres");
        }
        validarPreco(produto.getPreco());
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
    }
}
