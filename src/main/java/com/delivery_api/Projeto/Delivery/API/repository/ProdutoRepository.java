package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    //Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Long restauranteId);

    //Buscar produtos por categoria
    List<Produto> findByCategoriaIgnoreCase(String categoria);

    //Buscar produtos por disponibilidade
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

    List<Produto> findByCategoria(String categoria);

    List<Produto> findByDescricaoContaining(String descricao);

    List<Produto> findByDisponivelFalse();

    boolean existsByNomeAndRestaurante(String nome, Restaurante restaurante);


}
