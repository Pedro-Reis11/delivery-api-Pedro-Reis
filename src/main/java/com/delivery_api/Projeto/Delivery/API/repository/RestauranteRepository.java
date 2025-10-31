package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar restaurantes por nome
    List<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Buscar restaurante por categoria
    List<Restaurante> findByCategoriaIgnoreCase (String categoria);

    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();

    //Ordenação por avaliação
    List<Restaurante> findByAvaliacaoGreaterThanEqualOrderByAvaliacaoDesc(Double avaliacao);

    List<Restaurante> findByTelefone(String telefone);

    List<Restaurante> findByTaxaEntregaGreaterThanEqual(BigDecimal taxaEntrega);

    List<Restaurante> findByAtivoFalse();

    boolean existsByNome(String nome);
}
