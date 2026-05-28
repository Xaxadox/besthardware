package com.omni.besthardware.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CpuModel extends ComponenteModel {

        @NotBlank
        @Size(max = 128)
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
        
        @NotBlank
        @Size(max = 64)
        @Column(name = "socket", nullable = false, length = 64)
        private String socket;
}
