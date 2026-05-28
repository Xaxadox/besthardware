package com.omni.besthardware.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ComponenteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 64)
    @Column(name = "tipo", nullable = false, length = 64)
    private String tipo;

    @Column(name= "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @JsonIgnore
    @ManyToMany(mappedBy = "componentes")
    private Set<PerfilModel> perfis = new HashSet<>();
    //Indica que a relacao esta sendo gerenciada pelo PerfilModel

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "compatibilidade",
            joinColumns = @JoinColumn(name = "idComponente"),
            inverseJoinColumns = @JoinColumn(name = "idComponenteCompativel")
    )
    private Set<ComponenteModel> componentesCompativeis = new HashSet<>();
}
