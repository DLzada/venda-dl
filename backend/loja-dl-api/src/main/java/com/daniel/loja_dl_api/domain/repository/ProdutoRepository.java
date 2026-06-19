package com.daniel.loja_dl_api.domain.repository;

import com.daniel.loja_dl_api.domain.model.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCategoriaId(Long categoriaID);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNome(String nome);

    long countByCategoriaId(Long categoriaId);

    Page<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax, Pageable pageable);
}
