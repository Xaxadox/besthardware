package com.omni.besthardware.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ram")
public class ramModel {

        @Id
        private int idComponente;

        @Column(name = "tipo", length = 4)
        private char tipo;

        @Column(name = "frequencia")
        private int frequencia;

        @Column(name = "marca", length = 128)
        private String marca;


}
