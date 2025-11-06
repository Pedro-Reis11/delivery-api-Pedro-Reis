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

    //Buscar produtos por restaurante
    List<Produto> findByRestauranteId(Long restauranteId);

    //Buscar produtos por categoria
    List<Produto> findByCategoriaIgnoreCase(String categoria);

    //Buscar produtos por disponibilidade
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);

    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

    /**
     * Buscar produtos por faixa de preço
     */
    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);

    boolean existsByNomeAndRestaurante(String nome, Restaurante restaurante);

    /**
     * Produtos mais vendidos (top 10)
     * Conta quantas vezes cada produto aparece em itens de pedido
     */
    @Query("SELECT ip.produto, COUNT(ip) as total FROM ItemPedido ip " +
            "GROUP BY ip.produto ORDER BY total DESC")
    List<Object[]> findProdutosMaisVendidos();

    /**
     * Produtos mais vendidos com limite customizável
     */
    @Query(value = "SELECT p.*, COUNT(ip.id) as vendas " +
            "FROM produtos p " +
            "INNER JOIN itens_pedido ip ON p.id = ip.produto_id " +
            "GROUP BY p.id " +
            "ORDER BY vendas DESC " +
            "LIMIT :limite",
            nativeQuery = true)
    List<Produto> findTopProdutosMaisVendidos(@Param("limite") int limite);

    /**
     * Buscar produtos por restaurante e faixa de preço
     */
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId " +
            "AND p.preco BETWEEN :precoMin AND :precoMax")
    List<Produto> buscarPorRestauranteEFaixaPreco(
            @Param("restauranteId") Long restauranteId,
            @Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax
    );

    List<Produto> findByDisponivelTrue();
}
