package com.omni.besthardware.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Essencial para chaves compostas no Hibernate
public class perfilComponenteModel implements Serializable {

    @Column(name = "idPerfil")
    private Integer idPerfil;

    @Column(name = "idComponente")
    private Integer idComponente;
}