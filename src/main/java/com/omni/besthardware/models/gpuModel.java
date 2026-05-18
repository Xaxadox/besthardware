package com.omni.besthardware.models;

import jakarta.persistence.*;

@Entity
@Table(name = "gpu")
public class gpuModel {
    @Id
    private int idComponente;

    @Column(name = "modelo", length = 128)
    private String modelo;

    @Column(name = "memoria")
    private int memoria;

    @Column(name = "consumo")
    private int consumo;

    @Column(name = "marca", length = 128)
    private String marca;






}
