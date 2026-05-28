package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monitor")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class MonitorModel extends ComponenteModel {

    @Column(name = "marca", nullable = false, length = 128)
    private String marca;

    @Column(name = "tamanho", nullable = false, length = 2)
    private String tamanho;

    @Column(name = "resolucao", nullable = false, length = 64)
    private String resolucao;

    @Column(name = "frequencia", nullable = false)
    private Integer frequencia;

    @Column(name = "tecnologia", nullable = false, length = 64)
    private String tecnologia;
}
