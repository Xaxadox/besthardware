package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gpu")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class gpuModel extends componenteModel {

    @Column(name = "modelo", nullable = false, length = 128)
    private String modelo;

    @Column(name = "memoria", nullable = false)
    private Integer memoria;

    @Column(name = "consumo", nullable = false)
    private Integer consumo;

    @Column(name = "marca", nullable = false, length = 128)
    private String marca;
}