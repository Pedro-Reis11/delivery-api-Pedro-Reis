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

    List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

    //Buscar pedido por cliente
    List<Pedido> findByClienteId(Long clienteId);

    //Filtrar por status/data
    List<Pedido> findByStatus(PedidoStatus status);

    /**
     * Buscar top 10 pedidos mais recentes
     */
    List<Pedido> findTop10ByOrderByDataPedidoDesc();

    /**
     * Buscar pedidos entre datas específicas
     */
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
}

