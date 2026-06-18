package com.daniel.loja_dl_api.domain.repository;

import com.daniel.loja_dl_api.domain.model.entity.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCarrinhoRespository extends JpaRepository<ItemCarrinho, Long> {
}
