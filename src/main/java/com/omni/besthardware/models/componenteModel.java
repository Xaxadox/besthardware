package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="componente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class componenteModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo", nullable = false, length = 64)
    private String tipo;

    @Column(name= "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToMany(mappedBy = "componentes")
    private Set<perfilModel> perfis = new HashSet<>();
    //Indica que a relacao esta sendo gerenciada pelo PerfilModel
    
    @ManyToMany
    @JoinTable(
        name = "compatibilidade",
        joinColumns = @JoinColumn(name = "idComponente"),
        inverseJoinColumns = @JoinColumn(name = "idComponenteCompativel")
    )
    
    private Set<componenteModel> componentesCompativeis = new HashSet<>();
}
