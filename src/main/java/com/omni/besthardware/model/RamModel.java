package com.omni.besthardware.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Entidade JPA que representa Ram no projeto BestHardware.
 */
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

        @NotNull
        @Positive
        @Column(name = "frequencia", nullable = false)
        private Integer frequencia;

        @NotBlank
        @Size(max = 128)
        @Column(name = "marca", nullable = false, length = 128)
        private String marca;

        @NotNull
        @Positive
        @Column(name = "memoria", nullable = false)
        private Integer memoria;
}

