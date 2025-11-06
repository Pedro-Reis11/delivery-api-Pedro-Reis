package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.DTO.DTOMapper;
import com.delivery_api.Projeto.Delivery.API.DTO.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.exception.EntityNotFoundException;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private DTOMapper dtoMapper;

    /**
     * Cadastrar novo restaurante
     */
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO requestDTO) {
        // Validar se nome já existe
        if (restauranteRepository.existsByNome(requestDTO.getNome())) {
            throw new BusinessException("Restaurante já existe: " + requestDTO.getNome());
        }

        // Validar taxa de entrega
        if (requestDTO.getTaxaEntrega() != null &&
                requestDTO.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega deve ser maior ou igual a zero");
        }

        // Criar entidade Restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(requestDTO.getNome());
        restaurante.setCategoria(requestDTO.getCategoria());
        restaurante.setEndereco(requestDTO.getEndereco());
        restaurante.setTelefone(requestDTO.getTelefone());
        restaurante.setTaxaEntrega(requestDTO.getTaxaEntrega());
        restaurante.setAtivo(true);
        restaurante.setDataCadastro(LocalDateTime.now());

        // Salvar e retornar DTO
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return dtoMapper.toRestauranteResponseDTO(restauranteSalvo);
    }

    /**
     * Listar restaurantes ativos
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarAtivos() {
        return restauranteRepository.findByAtivoTrue()
                .stream()
                .map(dtoMapper::toRestauranteResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar restaurante por ID
     */
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorId(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        return dtoMapper.toRestauranteResponseDTO(restaurante);
    }

    /**
     * Buscar restaurantes por categoria
     */
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(dtoMapper::toRestauranteResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar restaurante
     */
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO requestDTO) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        // Verificar se nome não está sendo usado por outro restaurante
        if (!restaurante.getNome().equals(requestDTO.getNome()) &&
                restauranteRepository.existsByNome(requestDTO.getNome())) {
            throw new BusinessException("Já existe um restaurante com este nome");
        }

        // Validar taxa de entrega
        if (requestDTO.getTaxaEntrega() != null &&
                requestDTO.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega deve ser maior ou igual a zero");
        }

        // Atualizar campos
        restaurante.setNome(requestDTO.getNome());
        restaurante.setCategoria(requestDTO.getCategoria());
        restaurante.setEndereco(requestDTO.getEndereco());
        restaurante.setTelefone(requestDTO.getTelefone());
        restaurante.setTaxaEntrega(requestDTO.getTaxaEntrega());

        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);
        return dtoMapper.toRestauranteResponseDTO(restauranteAtualizado);
    }

    /**
     * Inativar restaurante (soft delete)
     */
    public void inativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        restaurante.inativar();
        restauranteRepository.save(restaurante);
    }
}