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
 * Entidade JPA que representa Fonte no projeto BestHardware.
 */
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

    @NotNull
    @Positive
    @Column(name = "potencia", nullable = false)
    private Integer potencia;

    @NotBlank
    @Size(max = 128)
    @Column(name = "certificacao", nullable = false, length = 128)
    private String certificacao;
}

