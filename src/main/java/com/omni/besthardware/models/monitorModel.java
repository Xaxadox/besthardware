package com.omni.besthardware.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "monitor")
public class monitorModel {

    @Id
    private int idComponente;

    @Column(name = "marca", length = 128)
    private String marca;

    @Column(name = "tamanho", length = 2)
    private char tamanho;

    @Column(name = "resolucao", length = 64)
    private String resolucao;

    @Column(name = "frequencia")
    private int frequencia;

    @Column(name = "tipo", length = 64)
    private String tipo;


}
