package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "cpu")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class cpuModel extends componenteModel {

        @Column(name = "modelo", nullable = false, length = 128)
        private String modelo;

        @Column(name = "frequencia", nullable = false)
        private Integer frequencia;

        @Column(name = "consumo", nullable = false)
        private Integer consumo;

        @Column(name = "anoLancamento", nullable = false)
        private LocalDate anoLancamento;

        @Column(name = "nucleos", nullable = false)
        private Integer nucleos;
}