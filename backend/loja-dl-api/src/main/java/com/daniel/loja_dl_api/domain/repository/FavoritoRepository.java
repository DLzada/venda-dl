package com.daniel.loja_dl_api.domain.repository;

import com.daniel.loja_dl_api.domain.model.entity.Favorito;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuarioId(Long usuarioId);

    Optional<Favorito> findByUsuarioAndProduto(Usuario usuario, Produto produto);

    boolean existsByUsuarioAndProduto(Usuario usuario, Produto produto);
}
