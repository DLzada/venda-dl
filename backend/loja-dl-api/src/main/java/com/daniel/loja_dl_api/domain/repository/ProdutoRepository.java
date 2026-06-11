package com.daniel.loja_dl_api.domain.repository;

import com.daniel.loja_dl_api.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaId(Long categoriaID);

    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
