package com.delivery_api.Projeto.Delivery.API.service.impl;

import com.delivery_api.Projeto.Delivery.API.DTO.request.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.exception.EntityNotFoundException;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import com.delivery_api.Projeto.Delivery.API.service.ProdutoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    // ==================================================
    // CADASTRAR PRODUTO
    // ==================================================
    @Override
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO requestDTO) {

        Restaurante restaurante = restauranteRepository.findById(requestDTO.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", requestDTO.getRestauranteId()));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Não é possível adicionar produtos a um restaurante inativo.");
        }

        if (produtoRepository.existsByNomeAndRestaurante(requestDTO.getNome(), restaurante)) {
            throw new BusinessException("Produto já existe neste restaurante: " + requestDTO.getNome());
        }

        if (requestDTO.getPreco() == null || requestDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço deve ser maior que zero.");
        }

        Produto produto = modelMapper.map(requestDTO, Produto.class);
        produto.setRestaurante(restaurante);
        produto.setDisponivel(true);

        Produto salvo = produtoRepository.save(produto);
        return modelMapper.map(salvo, ProdutoResponseDTO.class);
    }

    // ==================================================
    // BUSCAR POR ID
    // ==================================================
    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    // ==================================================
    // ATUALIZAR PRODUTO
    // ==================================================
    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        Restaurante restaurante = restauranteRepository.findById(requestDTO.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", requestDTO.getRestauranteId()));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Não é possível atualizar produtos de um restaurante inativo.");
        }

        if (requestDTO.getPreco() == null || requestDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço deve ser maior que zero.");
        }

        // Atualizar produto
        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setCategoria(requestDTO.getCategoria());
        produto.setPreco(requestDTO.getPreco());
        produto.setDisponivel(requestDTO.getDisponivel());
        produto.setRestaurante(restaurante);

        Produto atualizado = produtoRepository.save(produto);
        return modelMapper.map(atualizado, ProdutoResponseDTO.class);
    }

    // ==================================================
    // ATIVAR / DESATIVAR PRODUTO
    // ==================================================
    @Override
    public ProdutoResponseDTO ativarDesativarProduto(Long id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        produto.setDisponivel(!produto.getDisponivel());

        Produto atualizado = produtoRepository.save(produto);
        return modelMapper.map(atualizado, ProdutoResponseDTO.class);
    }

    // ==================================================
    // BUSCAR POR NOME
    // ==================================================
    @Override
    public ProdutoResponseDTO buscarPorNome(String nome) {

        Produto produto = produtoRepository.findByNome(nome)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + nome));

        if (!produto.getDisponivel()) {
            throw new BusinessException("Produto indisponível: " + nome);
        }

        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    // ==================================================
    // LISTAR POR RESTAURANTE
    // ==================================================
    @Override
    public List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId) {

        List<Produto> produtos = produtoRepository.findByRestauranteId(restauranteId);

        List<Produto> disponiveis = produtos.stream()
                .filter(Produto::getDisponivel)
                .toList();

        if (disponiveis.isEmpty()) {
            throw new BusinessException("Nenhum produto disponível para o restaurante ID: " + restauranteId);
        }

        return disponiveis.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    // ==================================================
    // BUSCAR POR CATEGORIA
    // ==================================================
    @Override
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {

        List<Produto> produtos = produtoRepository.findByCategoria(categoria);

        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado para a categoria: " + categoria);
        }

        return produtos.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    // ==================================================
    // BUSCAR POR FAIXA DE PREÇO
    // ==================================================
    @Override
    public List<ProdutoResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {

        List<Produto> produtos = produtoRepository.findByPrecoBetween(precoMinimo, precoMaximo);

        if (produtos.isEmpty()) {
            throw new BusinessException(
                    "Nenhum produto encontrado na faixa de preço entre " + precoMinimo + " e " + precoMaximo
            );
        }

        return produtos.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    // ==================================================
    // BUSCAR TODOS
    // ==================================================
    @Override
    public List<ProdutoResponseDTO> buscarTodosProdutos() {

        List<Produto> produtos = produtoRepository.findAll();

        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado.");
        }

        return produtos.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }

    // ==================================================
    // PREÇO <= VALOR
    // ==================================================
    @Override
    public List<ProdutoResponseDTO> buscarPorPrecoMenorOuIgual(BigDecimal valor) {

        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqual(valor);

        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado com preço menor ou igual a: " + valor);
        }

        return produtos.stream()
                .map(p -> modelMapper.map(p, ProdutoResponseDTO.class))
                .toList();
    }
}
