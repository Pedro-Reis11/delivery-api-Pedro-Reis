package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar restaurante por categoria
    List<Restaurante> findByCategoriaIgnoreCase (String categoria);

    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();

    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    List<Restaurante> findTop5ByOrderByNomeAsc();

    boolean existsByNome(String nome);

    /**
     * Total de vendas por restaurante (soma de todos os pedidos)
     */
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.restaurante.id = :restauranteId")
    BigDecimal calcularTotalVendasPorRestaurante(@Param("restauranteId") Long restauranteId);

    /**
     * Contar número de pedidos por restaurante
     */
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.restaurante.id = :restauranteId")
    Long contarPedidosPorRestaurante(@Param("restauranteId") Long restauranteId);

    /**
     * Buscar restaurantes por faixa de avaliação
     */
    @Query("SELECT r FROM Restaurante r WHERE r.avaliacao BETWEEN :min AND :max ORDER BY r.avaliacao DESC")
    List<Restaurante> buscarPorFaixaAvaliacao(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
}
