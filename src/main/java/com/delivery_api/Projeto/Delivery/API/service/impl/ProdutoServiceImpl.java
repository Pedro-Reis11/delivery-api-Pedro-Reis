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
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO requestDTO) {
        Restaurante restaurante = restauranteRepository.findById(requestDTO.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", requestDTO.getRestauranteId()));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Não é possível adicionar produtos a um restaurante inativo");
        }

        if (produtoRepository.existsByNomeAndRestaurante(requestDTO.getNome(), restaurante)) {
            throw new BusinessException("Produto já existe neste restaurante: " + requestDTO.getNome());
        }

        if (requestDTO.getPreco() == null || requestDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço deve ser maior que zero");
        }

        Produto produto = modelMapper.map(requestDTO, Produto.class);
        produto.setRestaurante(restaurante);
        produto.setDisponivel(true);

        Produto produtoSalvo = produtoRepository.save(produto);
        return modelMapper.map(produtoSalvo, ProdutoResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("A categoria não pode ser vazia");
        }

        return produtoRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarPorRestaurante(Long restauranteId) {
        List<Produto> produtos = (restauranteId == null)
                ? produtoRepository.findAll()
                : produtoRepository.findByRestauranteId(restauranteId);

        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId)
                .stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        if (!produto.getNome().equals(requestDTO.getNome()) &&
                produtoRepository.existsByNomeAndRestaurante(requestDTO.getNome(), produto.getRestaurante())) {
            throw new BusinessException("Já existe um produto com este nome neste restaurante");
        }

        if (requestDTO.getPreco() == null || requestDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço deve ser maior que zero");
        }

        modelMapper.map(requestDTO, produto);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return modelMapper.map(produtoAtualizado, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO ativarDesativar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        produto.setDisponivel(!produto.isDisponivel());
        Produto atualizado = produtoRepository.save(produto);

        return modelMapper.map(atualizado, ProdutoResponseDTO.class);
    }

}
