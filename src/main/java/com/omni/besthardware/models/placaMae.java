package com.omni.besthardware.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "placaMae")
public class placaMae {

    @Id
    private int idComponente;

    @Column(name = "marca", length = 128)
    private String marca;

    @Column(name = "socket", length = 64)
    private String socket;

    @Column(name = "chipset", length = 64)
    private String chipset;

    @Column(name = "formato", length = 32)
    private String formato;


}
