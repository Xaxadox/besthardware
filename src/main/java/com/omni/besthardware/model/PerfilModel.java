package com.omni.besthardware.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA que representa Perfil no projeto BestHardware.
 */
@Entity
@Table(name = "perfil")
@Getter
@Setter
@NoArgsConstructor
public class PerfilModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 128)
    @Column(name = "nome", nullable = false, length = 128)
    private String nome;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "perfilComponente",
            joinColumns = @JoinColumn(name = "idPerfil"),
            // A chave que aponta para esta classe (Perfil)
            inverseJoinColumns = @JoinColumn(name = "idComponente")
            // A chave que aponta para o outro lado
    )
    //Acesso a tabela perfilComponente, fiz dessa forma para nao quebrar a compatibilidade com o JPA
    private List<ComponenteModel> componentes = new ArrayList<>();
}
