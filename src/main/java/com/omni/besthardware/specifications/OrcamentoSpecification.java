package com.omni.besthardware.specifications;

import com.omni.besthardware.rest.dto.request.OrcamentoFiltroRequest;
import com.omni.besthardware.model.OrcamentoModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class OrcamentoSpecification {

    private OrcamentoSpecification() {
    }

    public static Specification<OrcamentoModel> comFiltros(OrcamentoFiltroRequest filtro) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.nome() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + filtro.nome().toLowerCase() + "%"
                ));
            }

            if (filtro.usuarioId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("usuario").get("id"), filtro.usuarioId()));
            }

            if (filtro.perfilId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("perfil").get("id"), filtro.perfilId()));
            }

            if (filtro.dataInicial() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.dataInicial()));
            }

            if (filtro.dataFinal() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.dataFinal()));
            }

            if (filtro.precoMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("preco"), filtro.precoMinimo()));
            }

            if (filtro.precoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("preco"), filtro.precoMaximo()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
