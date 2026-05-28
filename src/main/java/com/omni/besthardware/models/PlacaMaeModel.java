package com.omni.besthardware.models;

import jakarta.persistence.*;
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

    @Column(name = "marca", nullable = false, length = 128)
    private String marca;

    @Column(name = "socket", nullable = false, length = 64)
    private String socket;

    @Column(name = "chipset", nullable = false, length = 64)
    private String chipset;

    @Column(name = "formato", nullable = false, length = 32)
    private String formato;
}
