package com.delivery_api.Projeto.Delivery.API.service;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Restaurante cadastrar(Restaurante restaurante) {
        validarDadosRestaurante(restaurante);
        restaurante.setAtivo(true);
        restaurante.setDataCadastro(LocalDateTime.now());
        return restauranteRepository.save(restaurante);
    }

    @Transactional(readOnly = true)
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Restaurante buscarPorId(Long id) {
        return restauranteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoriaIgnoreCase(categoria);
    }

    public Restaurante atualizar(Long id, Restaurante atualizado) {
        Restaurante r = buscarPorId(id);
        if (!r.getNome().equals(atualizado.getNome()) && restauranteRepository.existsByNome(atualizado.getNome())) {
            throw new IllegalArgumentException("Já existe um restaurante com este nome");
        }
        r.setNome(atualizado.getNome());
        r.setCategoria(atualizado.getCategoria());
        r.setEndereco(atualizado.getEndereco());
        r.setTelefone(atualizado.getTelefone());
        r.setTaxaEntrega(atualizado.getTaxaEntrega());
        r.setAvaliacao(atualizado.getAvaliacao());
        if (r.getTaxaEntrega() != null && r.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de entrega deve ser maior ou igual a zero");
        }
        return restauranteRepository.save(r);
    }

    public void inativar(Long id) {
        Restaurante r = buscarPorId(id);
        r.inativar();
        restauranteRepository.save(r);
    }

    private void validarDadosRestaurante(Restaurante restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (restauranteRepository.existsByNome(restaurante.getNome())) {
            throw new IllegalArgumentException("Restaurante já existe: " + restaurante.getNome());
        }

        if (restaurante.getNome().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }

        if (restaurante.getTaxaEntrega() != null && restaurante.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de entrega deve ser maior ou igual a zero");
        }
    }
}