package com.omni.besthardware.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ofertaPreco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfertaPrecoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 128)
    @Column(name = "loja", nullable = false, length = 128)
    private String loja;

    @NotNull
    @Positive
    @Column(name = "precoAvista", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoAvista;

    @Positive
    @Column(name = "precoParcelado", precision = 10, scale = 2)
    private BigDecimal precoParcelado;

    @Positive
    @Column(name = "parcelas")
    private Integer parcelas;

    @Size(max = 64)
    @Column(name = "cupom", length = 64)
    private String cupom;

    @NotBlank
    @Size(max = 512)
    @Column(name = "urlProduto", nullable = false, length = 512)
    private String urlProduto;

    @NotBlank
    @Size(max = 255)
    @Column(name = "fonte", nullable = false, length = 255)
    private String fonte;

    @Size(max = 255)
    @Column(name = "observacoes", length = 255)
    private String observacoes;

    @NotNull
    @PastOrPresent
    @Column(name = "dataColeta", nullable = false)
    private LocalDate dataColeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idComponente", nullable = false)
    @JsonIgnore
    private ComponenteModel componente;
}
