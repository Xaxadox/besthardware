package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "itemOrcamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class itemOrcamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco", precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOrcamento", nullable = false)
    private orcamentoModel orcamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idComponente", nullable = false)
    private componenteModel componente;
}