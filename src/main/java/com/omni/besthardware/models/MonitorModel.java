package com.omni.besthardware.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 128)
    @Column(name = "marca", nullable = false, length = 128)
    private String marca;

    @NotBlank
    @Size(max = 2)
    @Column(name = "tamanho", nullable = false, length = 2)
    private String tamanho;

    @NotBlank
    @Size(max = 64)
    @Column(name = "resolucao", nullable = false, length = 64)
    private String resolucao;

    @Column(name = "frequencia", nullable = false)
    private Integer frequencia;

    @NotBlank
    @Size(max = 64)
    @Column(name = "tecnologia", nullable = false, length = 64)
    private String tecnologia;
}
