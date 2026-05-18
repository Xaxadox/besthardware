package com.omni.besthardware.models;


import jakarta.persistence.*;

@Entity
@Table(name = "perfilModel")
public class perfilModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 128)
    private String nome;

}
