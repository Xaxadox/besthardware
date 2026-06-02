package com.omni.besthardware.specifications;

import com.omni.besthardware.dtos.ComponenteFiltroRequest;
import com.omni.besthardware.models.ComponenteModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ComponenteSpecification {

    private ComponenteSpecification() {
    }

    public static Specification<ComponenteModel> comFiltros(ComponenteFiltroRequest filtro) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.tipo() != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("tipo")),
                        filtro.tipo().toLowerCase()
                ));
            }

            if (filtro.precoMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("preco"), filtro.precoMinimo()));
            }

            if (filtro.precoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("preco"), filtro.precoMaximo()));
            }

            if (filtro.perfilId() != null) {
                predicates.add(criteriaBuilder.equal(root.join("perfis").get("id"), filtro.perfilId()));
            }

            if (filtro.componenteCompativelId() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.join("componentesCompativeis").get("id"),
                        filtro.componenteCompativelId()
                ));
            }

            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
