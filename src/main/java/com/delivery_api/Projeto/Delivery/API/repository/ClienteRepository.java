package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Buscar cliente por email (método derivado)
    Optional<Cliente> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar clientes ativos
    List<Cliente> findByAtivoTrue();

    // Buscar clientes por nome (contendo)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    /**
     * Clientes com mais pedidos (top 10)
     */
    @Query("SELECT c, COUNT(p) as totalPedidos FROM Cliente c " +
            "LEFT JOIN Pedido p ON p.cliente = c " +
            "GROUP BY c " +
            "ORDER BY totalPedidos DESC")
    List<Object[]> findClientesComMaisPedidos();

    /**
     * Clientes inativos
     */
    List<Cliente> findByAtivoFalse();

    /**
     * Buscar clientes por parte do endereço
     */
    List<Cliente> findByEnderecoContainingIgnoreCase(String endereco);

    /**
     * Clientes cadastrados em um período
     */
    @Query("SELECT c FROM Cliente c WHERE c.dataCadastro BETWEEN :inicio AND :fim " +
            "ORDER BY c.dataCadastro DESC")
    List<Cliente> findClientesPorPeriodoCadastro(
            @Param("inicio") java.time.LocalDateTime inicio,
            @Param("fim") java.time.LocalDateTime fim
    );
}
