package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "itemOrcamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class itemOrcamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quantidade")
    private int quantidade;

    @Column(name = "preco", precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @Column(name = "idOrcamento")
    private int idOrcamento;

    @Column(name = "idComponente")
    private int idComponente;

}
