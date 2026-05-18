package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "armazenamento")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class armazenamentoModel extends componenteModel {

    @Column(name = "tipo", nullable = false, length = 3)
    private String tipo;

    @Column(name = "entrada", nullable = false, length = 4)
    private String entrada;

    @Column(name = "velocidadeEscrita", nullable = false)
    private Integer velocidadeEscrita;

    @Column(name = "velocidadeLeitura", nullable = false)
    private Integer velocidadeLeitura;
}