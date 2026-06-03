package com.omni.besthardware.specifications;

import com.omni.besthardware.rest.dto.request.FonteFiltroRequest;
import com.omni.besthardware.model.FonteModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class FonteSpecification {

    private FonteSpecification() {
    }

    public static Specification<FonteModel> comFiltros(FonteFiltroRequest filtro) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.tipo() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("tipo")), filtro.tipo().toLowerCase()));
            }

            if (filtro.precoMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("preco"), filtro.precoMinimo()));
            }

            if (filtro.precoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("preco"), filtro.precoMaximo()));
            }

            if (filtro.marca() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + filtro.marca().toLowerCase() + "%"));
            }

            if (filtro.potenciaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("potencia"), filtro.potenciaMinima()));
            }

            if (filtro.certificacao() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("certificacao")), "%" + filtro.certificacao().toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
