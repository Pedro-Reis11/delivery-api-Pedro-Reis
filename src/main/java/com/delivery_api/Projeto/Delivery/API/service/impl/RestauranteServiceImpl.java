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

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    // ==================================================
    // CADASTRAR
    // ==================================================
    @Override
    public RestauranteResponseDTO cadastrar(RestauranteRequestDTO dto) {

        // Verificar nome único
        if (restauranteRepository.findByNome(dto.getNome()).isPresent()) {
            throw new BusinessException("Restaurante já cadastrado: " + dto.getNome());
        }

        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);

        restaurante.setAtivo(true);
        restaurante.setDataCadastro(LocalDateTime.now());

        Restaurante salvo = restauranteRepository.save(restaurante);
        return modelMapper.map(salvo, RestauranteResponseDTO.class);
    }

    // ==================================================
    // BUSCAR POR ID
    // ==================================================
    @Override
    public RestauranteResponseDTO buscarPorId(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + id));

        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    // ==================================================
    // ATUALIZAR
    // ==================================================
    @Override
    public RestauranteResponseDTO atualizar(Long id, RestauranteRequestDTO dto) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + id));

        restaurante.setNome(dto.getNome());
        restaurante.setCategoria(dto.getCategoria());
        restaurante.setEndereco(dto.getEndereco());
        restaurante.setTelefone(dto.getTelefone());
        restaurante.setAvaliacao(dto.getAvaliacao());
        restaurante.setTaxaEntrega(dto.getTaxaEntrega());

        Restaurante atualizado = restauranteRepository.save(restaurante);
        return modelMapper.map(atualizado, RestauranteResponseDTO.class);
    }

    // ==================================================
    // ATIVAR/DESATIVAR
    // ==================================================
    @Override
    public RestauranteResponseDTO ativarDesativar(Long id) {

        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado: " + id));

        restaurante.setAtivo(!restaurante.isAtivo());
        Restaurante atualizado = restauranteRepository.save(restaurante);

        return modelMapper.map(atualizado, RestauranteResponseDTO.class);
    }

    // ==================================================
    // BUSCAR POR NOME
    // ==================================================
    @Override
    public RestauranteResponseDTO buscarPorNome(String nome) {

        Optional<Restaurante> opt = restauranteRepository.findByNome(nome);

        Restaurante restaurante = opt.orElseThrow(() ->
                new EntityNotFoundException("Restaurante não encontrado: " + nome));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Restaurante está desativado: " + nome);
        }

        return modelMapper.map(restaurante, RestauranteResponseDTO.class);
    }

    // ==================================================
    // BUSCAR POR CATEGORIA
    // ==================================================
    @Override
    public List<RestauranteResponseDTO> buscarPorCategoria(String categoria) {

        List<Restaurante> restaurantes = restauranteRepository.findByCategoriaIgnoreCase(categoria);

        if (restaurantes.isEmpty()) {
            throw new EntityNotFoundException("Nenhum restaurante encontrado na categoria: " + categoria);
        }

        return restaurantes.stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .toList();
    }

    // ==================================================
    // BUSCAR POR TAXA ENTREGA (intervalo)
    // ==================================================
    @Override
    public List<RestauranteResponseDTO> buscarPorPreco(BigDecimal min, BigDecimal max) {

        List<Restaurante> lista = restauranteRepository.findByTaxaEntregaBetween(min, max);

        if (lista.isEmpty()) {
            throw new EntityNotFoundException(
                    "Nenhum restaurante encontrado com taxa entre " + min + " e " + max
            );
        }

        return lista.stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .toList();
    }

    // ==================================================
    // LISTAR ATIVOS
    // ==================================================
    @Override
    public List<RestauranteResponseDTO> listarAtivos() {
        List<Restaurante> lista = restauranteRepository.findByAtivoTrue();

        if (lista.isEmpty()) {
            throw new EntityNotFoundException("Nenhum restaurante ativo encontrado.");
        }

        return lista.stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .toList();
    }

    // ==================================================
    // TOP 5
    // ==================================================
    @Override
    public List<RestauranteResponseDTO> buscarTop5PorNomeAsc() {

        List<Restaurante> lista = restauranteRepository.findTop5ByOrderByNomeAsc();

        if (lista.isEmpty()) {
            throw new EntityNotFoundException("Nenhum restaurante encontrado.");
        }

        return lista.stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .toList();
    }

    // ==================================================
    // RELATÓRIO DE VENDAS (projection)
    // ==================================================
    @Override
    public List<RelatorioVendas> relatorioVendasPorRestaurante() {
        List<RelatorioVendas> relatorio = restauranteRepository.relatorioVendasPorRestaurante();

        if (relatorio.isEmpty()) {
            throw new EntityNotFoundException("Nenhum dado de vendas encontrado.");
        }

        return relatorio;
    }

    // ==================================================
    // BUSCAR POR TAXA <=
    // ==================================================
    @Override
    public List<RestauranteResponseDTO> buscarPorTaxaEntrega(BigDecimal taxaEntrega) {

        List<Restaurante> lista = restauranteRepository.findByTaxaEntregaLessThanEqual(taxaEntrega);

        if (lista.isEmpty()) {
            throw new EntityNotFoundException(
                    "Nenhum restaurante com taxa <= " + taxaEntrega
            );
        }

        return lista.stream()
                .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
                .toList();
    }
}
