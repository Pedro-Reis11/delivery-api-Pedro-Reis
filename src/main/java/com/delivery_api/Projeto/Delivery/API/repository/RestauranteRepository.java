package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Restaurante;
import com.delivery_api.Projeto.Delivery.API.projection.RelatorioVendas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar por nome
    Optional<Restaurante> findByNome(String nome);

    //buscar por nome e ativo
    Restaurante findByNomeAndAtivoTrue(String nome);

    // Buscar restaurante por categoria
    List<Restaurante> findByCategoriaIgnoreCase (String categoria);

    // Buscar restaurantes ativos
    List<Restaurante> findByAtivoTrue();

    List<Restaurante> findByTaxaEntregaLessThanEqual(BigDecimal taxa);

    List<Restaurante> findTop5ByOrderByNomeAsc();

    List<Restaurante> findByTaxaEntregaBetween(BigDecimal precoMinimo, BigDecimal precoMaximo);

    boolean existsByNome(String nome);

    /**
     * Relatório de vendas por restaurante
     * Retorna nome, total de vendas e quantidade de pedidos
     */
    @Query("""
        SELECT r.nome as nomeRestaurante,
               SUM(p.total) as totalVendas,
               COUNT(p.id) as quantidadePedidos
        FROM Restaurante r
        LEFT JOIN Pedido p ON r.id = p.restaurante.id
        GROUP BY r.id, r.nome
        ORDER BY totalVendas DESC
        """)
    List<RelatorioVendas> relatorioVendasPorRestaurante();

}
