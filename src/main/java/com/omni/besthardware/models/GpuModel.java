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
@Table(name = "gpu")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class GpuModel extends ComponenteModel {

    @NotBlank
    @Size(max = 128)
    @Column(name = "modelo", nullable = false, length = 128)
    private String modelo;

    @NotNull
    @Positive
    @Column(name = "memoria", nullable = false)
    private Integer memoria;

    @NotNull
    @Positive
    @Column(name = "consumo", nullable = false)
    private Integer consumo;

    @NotBlank
    @Size(max = 128)
    @Column(name = "marca", nullable = false, length = 128)
    private String marca;
}

