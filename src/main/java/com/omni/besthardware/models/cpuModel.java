package com.omni.besthardware.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "cpu")
public class cpuModel {
        @Id
        private Integer idComponente;

        @Column(name = "modelo", length = 128)
        private String modelo;

        @Column(name = "frequencia")
        private int frequencia;

        @Column(name = "consumo")
        private int consumo;

        @Column(name = "anoLancamento")
        private LocalDate anoLacamento;

        @Column(name = "nucleos")
        private int nucleos;


}
