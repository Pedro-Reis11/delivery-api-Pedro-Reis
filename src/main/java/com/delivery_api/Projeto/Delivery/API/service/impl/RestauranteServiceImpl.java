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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {
        // Validar nome único
        Optional<Restaurante> byNome = restauranteRepository.findByNome(dto.getNome());
        if (byNome.equals(dto.getNome())) {
            throw new BusinessException("Restaurante já cadastrado: " + dto.getNome());
        }
        // Converter DTO para entidade
        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);
        // Salvar cliente
        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        // Retornar DTO de resposta
        return modelMapper.map(restauranteSalvo, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO buscarPorId(Long id) {
        // Buscar restaurante por ID
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante não encontrado com ID: " + id));
        // Converter entidade para DTO
        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {
        // Buscar restaurante existente
        Restaurante restauranteExistente = restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante não encontrado com ID: " + id));

        // Atualizar campos do restaurante
        restauranteExistente.setNome(dto.getNome());
        restauranteExistente.setCategoria(dto.getCategoria());
        restauranteExistente.setTelefone(dto.getTelefone());
        restauranteExistente.setAvaliacao(dto.getAvaliacao());
        restauranteExistente.setEndereco(dto.getEndereco());
        restauranteExistente.setTelefone(dto.getTelefone());

        // Salvar as alterações
        Restaurante restauranteAtualizado = restauranteRepository.save(restauranteExistente);

        // Retornar DTO atualizado
        return modelMapper.map(restauranteAtualizado, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO ativarDesativar(Long id) {
        // Buscar restaurante por ID
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurante não encontrado com ID: " + id));
        // Alternar status de ativo/desativado
        restaurante.setAtivo(!restaurante.getAtivo());
        // Salvar as alterações
        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);
        // Retornar DTO atualizado
        return modelMapper.map(restauranteAtualizado, RestauranteResponseDTO.class);
    }

    @Override
    public RestauranteResponseDTO buscarPorNome(String nome) {
        // Buscar restaurante por nome
        Restaurante restaurante = restauranteRepository.findByNomeAndAtivoTrue(nome);
        if (!restaurante.getNome().equalsIgnoreCase(nome)) {
            throw new BusinessException("Restaurante não encontrado com nome: " + nome);
        }
        else if (!restaurante.getAtivo()) {
            throw new BusinessException("Restaurante está desativado: " + nome);
        }
        // Converter entidade para DTO
        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    @Override
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {
        // Buscar restaurantes por categoria
        List<Restaurante> restaurantes = restauranteRepository.findByCategoriaIgnoreCase(categoria);
        if (restaurantes.isEmpty()) {
            throw new BusinessException("Nenhum restaurante encontrado na categoria: " + categoria);
        }
        // Converter lista de entidades para lista de DTOs
        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    public List<RestauranteResponseDTO> buscarPorPreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        // Buscar restaurantes por taxa de entrega dentro do intervalo
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaBetween(precoMinimo, precoMaximo);
        if (restaurantes.isEmpty()) {
            throw new BusinessException("Nenhum restaurante encontrado com taxa de entrega entre " + precoMinimo + " e " + precoMaximo);
        }
        // Converter lista de entidades para lista de DTOs
        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    public List<RestauranteResponseDTO> listarAtivos() {
        // Buscar todos os restaurantes ativos
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();
        if (restaurantesAtivos.isEmpty()) {
            throw new BusinessException("Nenhum restaurante ativo encontrado.");
        }
        // Converter lista de entidades para lista de DTOs
        return restaurantesAtivos.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    public List<RestauranteResponseDTO> buscarTop5PorNomeAsc() {
        // Buscar os 5 primeiros restaurantes por nome
        List<Restaurante> top5Restaurantes = restauranteRepository.findTop5ByOrderByNomeAsc();
        if (top5Restaurantes.isEmpty()) {
            throw new BusinessException("Nenhum restaurante encontrado.");
        }
        // Converter lista de entidades para lista de DTOs
        return top5Restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }

    @Override
    public List<RelatorioVendas> relatorioVendasPorRestaurante() {
        // Buscar relatório de vendas por restaurante
        List<RelatorioVendas> relatorio = restauranteRepository.relatorioVendasPorRestaurante();
        if (relatorio.isEmpty()) {
            throw new BusinessException("Nenhum dado de vendas encontrado.");
        }
        // Converter lista de entidades para lista de DTOs
        return relatorio.stream()
                .map(restaurante -> modelMapper.map(restaurante, RelatorioVendas.class))
                .toList();
    }

    @Override
    public List<RestauranteResponseDTO> buscarPorTaxaEntrega(BigDecimal taxaEntrega) {
        // Buscar restaurantes por taxa de entrega
        List<Restaurante> restaurantes = restauranteRepository.findByTaxaEntregaLessThanEqual(taxaEntrega);
        if (restaurantes.isEmpty()) {
            throw new BusinessException("Nenhum restaurante encontrado com taxa de entrega menor ou igual a: " + taxaEntrega);
        }
        // Converter lista de entidades para lista de DTOs
        return restaurantes.stream()
                .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
                .toList();
    }
}
