package com.omni.besthardware.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamento")
public class orcamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 64)
    private String nome;

    @Column(name = "dataCriacao")
    private LocalDateTime dataCriacao;

    @Column(name = "preco", precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @Column(name =  "idUsuario")
    private int idUsuario;
}
