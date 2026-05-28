package com.omni.besthardware.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class ItemOrcamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Positive
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @NotNull
    @Positive
    @Column(name = "preco", precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOrcamento", nullable = false)
    @JsonIgnore
    private OrcamentoModel orcamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idComponente", nullable = false)
    private ComponenteModel componente;
}

