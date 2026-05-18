package com.omni.besthardware.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fonte")
public class fonteModel {

    @Id
    private int idComponente;


    @Column(name = "marca", length = 128)
    private int marca;

    @Column(name = "potencia")
    private int potencia;

    @Column(name = "certificacao", length = 128)
    private String certificacao;
    
}
