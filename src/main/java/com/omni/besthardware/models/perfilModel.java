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
    @ManyToMany(fetch = FetchType.LAZY)

    @JoinTable(name = "perfilComponente",
            joinColumns = @JoinColumn(name = "idPerfil"),
            // A chave que aponta para esta classe (Perfil)
            inverseJoinColumns = @JoinColumn(name = "idComponente")
            // A chave que aponta para o outro lado
    )
    //Acesso a tabela perfilComponente, fiz dessa forma para nao quebrar a compatibilidade com o JPA
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