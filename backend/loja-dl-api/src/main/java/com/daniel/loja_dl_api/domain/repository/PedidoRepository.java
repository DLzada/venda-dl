package com.daniel.loja_dl_api.domain.repository;

import com.daniel.loja_dl_api.domain.model.entity.Pedido;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);

    @EntityGraph(attributePaths = {"itens"})
    List<Pedido> findByStatus(StatusPedido status);
}
