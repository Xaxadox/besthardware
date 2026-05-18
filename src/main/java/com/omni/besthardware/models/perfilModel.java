package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "perfil")
@Getter
@Setter
@NoArgsConstructor
public class perfilModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 128)
    private String nome;

    // Abstração da tabela associativa perfilComponente
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfilComponente",
            joinColumns = @JoinColumn(name = "idPerfil"),
            inverseJoinColumns = @JoinColumn(name = "idComponente")
    )
    private List<componenteModel> componentes;
}