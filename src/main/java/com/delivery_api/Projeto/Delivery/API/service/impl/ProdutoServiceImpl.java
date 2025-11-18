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
import java.util.Optional;
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
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto", id));

        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO requestDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
        Optional<Restaurante> restaurante = restauranteRepository.findById(requestDTO.getRestauranteId());
        // Validar dados do produto
        if (requestDTO.getNome() == null || requestDTO.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (requestDTO.getDescricao() == null || requestDTO.getDescricao().isEmpty()) {
            throw new IllegalArgumentException("Descrição do produto é obrigatória");
        }
        if (requestDTO.getPreco() == null || requestDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
        if (requestDTO.getCategoria() == null || requestDTO.getCategoria().isEmpty()) {
            throw new IllegalArgumentException("Categoria do produto é obrigatória");
        }
        // Atualizar dados do produto
        produtoExistente.setNome(requestDTO.getNome());
        produtoExistente.setDescricao(requestDTO.getDescricao());
        produtoExistente.setPreco(requestDTO.getPreco());
        produtoExistente.setCategoria(requestDTO.getCategoria());
        produtoExistente.setDisponivel(requestDTO.getDisponivel());
        produtoExistente.setRestaurante(restaurante.get());
        // Salvar produto atualizado
        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        // Retornar DTO de resposta
        return modelMapper.map(produtoAtualizado, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO ativarDesativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado", id));

        produto.setDisponivel(!produto.getDisponivel());
        Produto atualizado = produtoRepository.save(produto);

        return modelMapper.map(atualizado, ProdutoResponseDTO.class);
    }

    @Override
    public ProdutoResponseDTO buscarPorNome(String nome) {
        // Buscar produto por nome
        Produto produto = produtoRepository.findByNome(nome);
        if(!produto.getDisponivel()){
            throw new BusinessException ("Produto indisponível: " + nome);
        }
        // Converter entidade para DTO
        return modelMapper.map(produto, ProdutoResponseDTO.class);
    }

    @Override
    public List<ProdutoResponseDTO> buscarPorRestaurante(Long restauranteId) {
        // Buscar produtos por restaurante ID
        List<Produto> produtos = produtoRepository.findByRestauranteId(restauranteId);
        if (produtos.isEmpty() || produtos.stream().noneMatch(Produto::getDisponivel)) {
            throw new BusinessException("Nenhum produto encontrado para o restaurante ID: " + restauranteId);
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .filter(Produto::getDisponivel) // Filtrar apenas produtos disponíveis
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {
        // Buscar produtos por categoria
        List<Produto> produtos = produtoRepository.findByCategoria(categoria);
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado para a categoria: " + categoria);
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        // Buscar produtos por faixa de preço
        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqual(precoMaximo);
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado na faixa de preço: " + precoMinimo + " a " + precoMaximo);
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .filter(produto -> produto.getPreco().compareTo(precoMinimo) >= 0)
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

    @Override
    public List<ProdutoResponseDTO> buscarTodosProdutos() {
        // Buscar todos os produtos
        List<Produto> produtos = produtoRepository.findAll();
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado");
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }
    //buscar produtos com preço menor ou igual a um valor específico
    @Override
    public List<ProdutoResponseDTO> buscarPorPrecoMenorOuIgual(BigDecimal valor) {
        // Buscar produtos com preço menor ou igual ao valor especificado
        List<Produto> produtos = produtoRepository.findByPrecoLessThanEqual(valor);
        if (produtos.isEmpty()) {
            throw new BusinessException("Nenhum produto encontrado com preço menor ou igual a: " + valor);
        }
        // Converter lista de entidades para lista de DTOs
        return produtos.stream()
                .map(produto -> modelMapper.map(produto, ProdutoResponseDTO.class))
                .toList();
    }

}
