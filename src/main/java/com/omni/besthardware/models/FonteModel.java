package com.omni.besthardware.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fonte")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class FonteModel extends ComponenteModel {

    @NotBlank
    @Size(max = 128)
    @Column(name = "marca", nullable = false, length = 128)
    private String marca;

    @Column(name = "potencia", nullable = false)
    private Integer potencia;

    @NotBlank
    @Size(max = 128)
    @Column(name = "certificacao", nullable = false, length = 128)
    private String certificacao;
}
