package com.omni.besthardware.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "placaMae")
@PrimaryKeyJoinColumn(name = "idComponente")
@Getter
@Setter
@NoArgsConstructor
public class PlacaMaeModel extends ComponenteModel {

    @NotBlank
    @Size(max = 128)
    @Column(name = "marca", nullable = false, length = 128)
    private String marca;

    @NotBlank
    @Size(max = 64)
    @Column(name = "socket", nullable = false, length = 64)
    private String socket;

    @NotBlank
    @Size(max = 64)
    @Column(name = "chipset", nullable = false, length = 64)
    private String chipset;

    @NotBlank
    @Size(max = 32)
    @Column(name = "formato", nullable = false, length = 32)
    private String formato;
}
