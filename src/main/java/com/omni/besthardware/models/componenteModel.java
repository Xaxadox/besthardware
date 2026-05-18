package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="componente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class componenteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome",nullable = false, length = 64)
    private String nome;

    @Column(name= "valor",nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "idPerfil")
    @ManyToOne(fetch = FetchType.LAZY)
    private Integer idPerfil;
}
