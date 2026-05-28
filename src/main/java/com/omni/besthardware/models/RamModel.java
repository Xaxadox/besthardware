package com.omni.besthardware.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ram")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class RamModel extends ComponenteModel {

        @NotBlank
        @Size(max = 64)
        @Column(name = "geracao", nullable = false, length = 64)
        private String geracao;

        @Column(name = "frequencia", nullable = false)
        private Integer frequencia;

        @NotBlank
        @Size(max = 128)
        @Column(name = "marca", nullable = false, length = 128)
        private String marca;

        @Column(name = "memoria", nullable = false)
        private Integer memoria;
}
