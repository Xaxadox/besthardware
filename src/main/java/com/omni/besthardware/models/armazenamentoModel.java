package com.omni.besthardware.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "armazenamento")
public class armazenamentoModel {

    @Id
    private Integer idComponente;

    @Column(name = "tipo", length = 3)
    private char tipo;

    @Column(name = "entrada", length = 4)
    private char entrada;

    @Column(name = "velocidadeEscrita")
    private int velocidadeEscrita;

    @Column(name = "velocidadeLeitura")
    private int velocidadeLeitura;

}
