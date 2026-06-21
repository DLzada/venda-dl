package com.daniel.loja_dl_api.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_cupom")
@NoArgsConstructor
@Getter
@Setter
public class Cupom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false)
    private BigDecimal porcentagemDesconto;

    @Column(nullable = false)
    private LocalDate dataValidade;
}
