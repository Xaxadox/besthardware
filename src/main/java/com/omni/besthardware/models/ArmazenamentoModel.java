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
public class ArmazenamentoModel extends ComponenteModel {

    @Column(name = "tecnologia", nullable = false, length = 64)
    private String tecnologia;
    //HDD ou SSD

    @Column(name = "padrao", nullable = false, length = 64)
    private String padrao;
    //SATA OU NVME

    @Column(name = "velocidadeEscrita", nullable = false)
    private Integer velocidadeEscrita;

    @Column(name = "velocidadeLeitura", nullable = false)
    private Integer velocidadeLeitura;

    @Column(name = "memoria", nullable = false)
    private Integer memoria;
}
