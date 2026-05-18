package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fonte")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class fonteModel extends componenteModel {

    @Column(name = "marca", nullable = false, length = 128)
    private String marca;

    @Column(name = "potencia", nullable = false)
    private Integer potencia;

    @Column(name = "certificacao", nullable = false, length = 128)
    private String certificacao;
}