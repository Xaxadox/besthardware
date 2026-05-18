package com.omni.besthardware.models;

import jakarta.persistence.*;




@Entity
@Table(name = "usuario")
public class usuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "email", length = 255, nullable = false)
    private String email;



}
