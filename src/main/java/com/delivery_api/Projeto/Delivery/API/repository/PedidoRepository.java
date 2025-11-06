package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import com.delivery_api.Projeto.Delivery.API.enums.PedidoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    //Buscar pedido por cliente
    List<Pedido> findByClienteId(Long clienteId);

    //Filtrar por status/data
    List<Pedido> findByStatus(PedidoStatus status);
    List<Pedido> findByDataCriacaoBetween(LocalDateTime start, LocalDateTime end);

    // Filtrar pedidos por cliente e status
    List<Pedido> findByClienteIdAndStatus(Long clienteId, PedidoStatus status);

    // Filtrar pedidos por cliente e data
    List<Pedido> findByClienteIdAndDataCriacaoGreaterThanEqual(Long clienteId, LocalDateTime dataCriacao);

    Pedido findByNumeroPedido(String numeroPedido);

    List<Pedido> findByDataPedidoGreaterThanEqual(LocalDateTime dataPedido);

    List<Pedido> findByObservacoesContaining(String observacoes);

    List<Pedido> findByRestauranteId(Long restauranteId);

    // Relatório: Contar pedidos por status
    long countByStatus(PedidoStatus status);

    // Relatório: Somar total de valores de pedidos por status
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.status = :status")
    BigDecimal sumTotalByStatus(@Param("status") PedidoStatus status);

    // Relatório: Somar total de valores de pedidos por cliente
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.cliente.id = :clienteId")
    BigDecimal sumTotalByClienteId(@Param("clienteId") Long clienteId);

    // Relatório: Listar pedidos em um período (data de criação entre duas datas)
    @Query("SELECT p FROM Pedido p WHERE p.dataCriacao BETWEEN :dataInicio AND :dataFim")
    List<Pedido> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    // Relatório: Contar pedidos por cliente
    long countByClienteId(Long clienteId);

    /**
     * Buscar top 10 pedidos mais recentes
     */
    List<Pedido> findTop10ByOrderByDataPedidoDesc();

    /**
     * Buscar pedidos entre datas específicas
     */
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Pedidos com valor acima de um determinado valor
     */
    @Query("SELECT p FROM Pedido p WHERE p.total > :valorMinimo ORDER BY p.total DESC")
    List<Pedido> findPedidosAcimaDeValor(@Param("valorMinimo") BigDecimal valorMinimo);

    /**
     * Relatório de pedidos por período e status
     */
    @Query("SELECT p FROM Pedido p WHERE p.dataCriacao BETWEEN :inicio AND :fim " +
            "AND p.status = :status ORDER BY p.dataCriacao DESC")
    List<Pedido> relatorioPorPeriodoEStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") PedidoStatus status
    );

    /**
     * Ranking de clientes por número de pedidos (Query Nativa)
     */
    @Query(value = "SELECT c.id, c.nome, COUNT(p.id) as total_pedidos " +
            "FROM clientes c " +
            "INNER JOIN pedidos p ON c.id = p.cliente_id " +
            "GROUP BY c.id, c.nome " +
            "ORDER BY total_pedidos DESC " +
            "LIMIT :limite",
            nativeQuery = true)
    List<Object[]> rankingClientesPorPedidos(@Param("limite") int limite);

    /**
     * Faturamento por categoria de restaurante
     */
    @Query("SELECT r.categoria, SUM(p.total) as faturamento " +
            "FROM Pedido p JOIN p.restaurante r " +
            "WHERE p.status = 'ENTREGUE' " +
            "GROUP BY r.categoria " +
            "ORDER BY faturamento DESC")
    List<Object[]> faturamentoPorCategoria();

    /**
     * Pedidos agrupados por status com contagem
     */
    @Query("SELECT p.status, COUNT(p), SUM(p.total) FROM Pedido p " +
            "GROUP BY p.status")
    List<Object[]> resumoPedidosPorStatus();
}
