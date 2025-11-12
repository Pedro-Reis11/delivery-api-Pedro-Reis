package com.delivery_api.Projeto.Delivery.API.service.impl;

import com.delivery_api.Projeto.Delivery.API.DTO.request.RestauranteRequestDTO;
import com.delivery_api.Projeto.Delivery.API.DTO.response.RestauranteResponseDTO;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.exception.BusinessException;
import com.delivery_api.Projeto.Delivery.API.exception.EntityNotFoundException;
import com.delivery_api.Projeto.Delivery.API.projection.RelatorioVendas;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import com.delivery_api.Projeto.Delivery.API.service.RestauranteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO requestDTO) {
        if (restauranteRepository.existsByNome(requestDTO.getNome())) {
            throw new BusinessException("Restaurante já existe: " + requestDTO.getNome());
        }

        if (requestDTO.getTaxaEntrega() != null &&
                requestDTO.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega deve ser maior ou igual a zero");
        }

        Restaurante restaurante = modelMapper.map(requestDTO, Restaurante.class);
        restaurante.setAtivo(true);
        restaurante.setDataCadastro(LocalDateTime.now());

        Restaurante salvo = restauranteRepository.save(restaurante);
        return modelMapper.map(salvo, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarAtivos() {
        return restauranteRepository.findByAtivoTrue()
                .stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarPorId(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO requestDTO) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        if (!restaurante.getNome().equals(requestDTO.getNome()) &&
                restauranteRepository.existsByNome(requestDTO.getNome())) {
            throw new BusinessException("Já existe um restaurante com este nome");
        }

        if (requestDTO.getTaxaEntrega() != null &&
                requestDTO.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega deve ser maior ou igual a zero");
        }

        modelMapper.map(requestDTO, restaurante);
        Restaurante atualizado = restauranteRepository.save(restaurante);
        return modelMapper.map(atualizado, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO ativarDesativar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante", id));

        restaurante.setAtivo(!restaurante.isAtivo());

        Restaurante atualizado = restauranteRepository.save(restaurante);
        return modelMapper.map(atualizado, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarPorTaxaEntregaMenorOuIgual(BigDecimal taxa) {
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaLessThanEqual(taxa);

        if (restaurantes.isEmpty()) {
            throw new EntityNotFoundException("Nenhum restaurante encontrado com taxa de entrega menor ou igual a " + taxa);
        }

        return restaurantes.stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarTop5PorNomeAsc() {
        List<Restaurante> top5 = restauranteRepository.findTop5ByOrderByNomeAsc();

        if (top5.isEmpty()) {
            throw new EntityNotFoundException("Nenhum restaurante encontrado para o ranking Top 5.");
        }

        return top5.stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelatorioVendas> relatorioVendasPorRestaurante() {
        return restauranteRepository.relatorioVendasPorRestaurante();
    }


    private void validarTaxaEntrega(RestauranteRequestDTO requestDTO) {
        if (requestDTO.getTaxaEntrega() != null &&
                requestDTO.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Taxa de entrega deve ser maior ou igual a zero");
        }
    }
}
