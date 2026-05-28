package com.omni.besthardware.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 64)
    @Column(name = "tecnologia", nullable = false, length = 64)
    private String tecnologia;
    //HDD ou SSD

    @NotBlank
    @Size(max = 64)
    @Column(name = "padrao", nullable = false, length = 64)
    private String padrao;
    //SATA OU NVME

    @NotNull
    @Positive
    @Column(name = "velocidadeEscrita", nullable = false)
    private Integer velocidadeEscrita;

    @NotNull
    @Positive
    @Column(name = "velocidadeLeitura", nullable = false)
    private Integer velocidadeLeitura;

    @NotNull
    @Positive
    @Column(name = "memoria", nullable = false)
    private Integer memoria;
}

