package com.daniel.loja_dl_api.domain.repository;

import com.daniel.loja_dl_api.domain.model.entity.Pedido;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);

    @EntityGraph(attributePaths = {"itens"})
    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByUsuarioIdOrderByDataPedidoDesc(Long usuarioId);

    List<Pedido> findByStatusAndDataPedidoBetween(StatusPedido status, LocalDateTime dataInicio, LocalDateTime dataFim);

    Optional<Pedido> findByUrlPagamentoContaining(String url);
}
