package com.omni.besthardware.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orcamento")
@Getter
@Setter
@NoArgsConstructor
public class OrcamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 64)
    private String nome;

    @Column(name = "dataCriacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPerfil", nullable = false)
    private PerfilModel perfil;

    // Relação bidirecional (opcional, dependendo das consultas necessárias)
    @OneToMany(mappedBy = "orcamento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemOrcamentoModel> itens = new ArrayList<>();
}
