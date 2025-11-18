package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Produto;
import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // buscar produto por restaurante ID
    List<Produto> findByRestauranteId(Long restauranteId);

    // Apenas produtos disponíveis
    List<Produto> findByDisponivelTrue();

    // Produtos por categoria
    List<Produto> findByCategoria(String categoria);

    // Por faixa de preço (menor ou igual)
    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

    // Buscar produto por nome
    Produto findByNome(String nome);

    boolean existsByNomeAndRestaurante(String nome, Restaurante restaurante);
}
