package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTO.DTOMapper;
import com.delivery_api.Projeto.Delivery.API.DTO.ProdutoRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.ProdutoResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.exception.EntityNotFoundException;
import com.delivery_api.Projeto.Delivery.API.repository.ProdutoRepository;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private DTOMapper dtoMapper;

    /**
     * Cadastrar produto
     */
    public ProdutoResponseDTO cadastrar(ProdutoRequestDTO requestDTO) {
        // Buscar restaurante
        Restaurante restaurante = restauranteRepository.findById(requestDTO.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", requestDTO.getRestauranteId()));

        // Validar se restaurante está ativo
        if (!restaurante.isAtivo()) {
            throw new BusinessException("Não é possível adicionar produtos a um restaurante inativo");
        }

        // Validar se produto já existe neste restaurante
        if (produtoRepository.existsByNomeAndRestaurante(requestDTO.getNome(), restaurante)) {
            throw new BusinessException("Produto já existe neste restaurante: " + requestDTO.getNome());
        }

        // Validar preço
        if (requestDTO.getPreco() == null || requestDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço deve ser maior que zero");
        }

        // Criar entidade Produto
        Produto produto = new Produto();
        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setPreco(requestDTO.getPreco());
        produto.setCategoria(requestDTO.getCategoria());
        produto.setRestaurante(restaurante);
        produto.setDisponivel(true);

        // Salvar e retornar DTO
        Produto produtoSalvo = produtoRepository.save(produto);
        return dtoMapper.toProdutoResponseDTO(produtoSalvo);
    }

    /**
     * Listar produtos por restaurante
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarPorRestaurante(Long restauranteId) {
        if (restauranteId == null) {
            return produtoRepository.findAll()
                    .stream()
                    .map(dtoMapper::toProdutoResponseDTO)
                    .collect(Collectors.toList());
        }

        return produtoRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(dtoMapper::toProdutoResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar produtos disponíveis por restaurante
     */
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarDisponiveisPorRestaurante(Long restauranteId) {
        return produtoRepository.findByRestauranteIdAndDisponivelTrue(restauranteId)
                .stream()
                .map(dtoMapper::toProdutoResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar produto por ID
     */
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        return dtoMapper.toProdutoResponseDTO(produto);
    }

    /**
     * Atualizar produto
     */
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        // Verificar se nome não está sendo usado por outro produto no mesmo restaurante
        if (!produto.getNome().equals(requestDTO.getNome()) &&
                produtoRepository.existsByNomeAndRestaurante(requestDTO.getNome(), produto.getRestaurante())) {
            throw new BusinessException("Já existe um produto com este nome neste restaurante");
        }

        // Validar preço
        if (requestDTO.getPreco() == null || requestDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Preço deve ser maior que zero");
        }

        // Atualizar campos
        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setPreco(requestDTO.getPreco());
        produto.setCategoria(requestDTO.getCategoria());

        Produto produtoAtualizado = produtoRepository.save(produto);
        return dtoMapper.toProdutoResponseDTO(produtoAtualizado);
    }

    /**
     * Inativar produto (soft delete)
     */
    public void inativar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        produto.setDisponivel(false);
        produtoRepository.save(produto);
    }

    /**
     * Ativar produto
     */
    public ProdutoResponseDTO ativar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        produto.setDisponivel(true);
        Produto produtoAtivado = produtoRepository.save(produto);
        return dtoMapper.toProdutoResponseDTO(produtoAtivado);
    }
}