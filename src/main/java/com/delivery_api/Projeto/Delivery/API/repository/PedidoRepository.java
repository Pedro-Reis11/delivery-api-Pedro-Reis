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


}
