package com.omni.besthardware.specifications;

import com.omni.besthardware.dtos.RamFiltroRequest;
import com.omni.besthardware.models.RamModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class RamSpecification {

    private RamSpecification() {
    }

    public static Specification<RamModel> comFiltros(RamFiltroRequest filtro) {
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

            if (filtro.geracao() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("geracao")), filtro.geracao().toLowerCase()));
            }

            if (filtro.frequenciaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("frequencia"), filtro.frequenciaMinima()));
            }

            if (filtro.marca() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + filtro.marca().toLowerCase() + "%"));
            }

            if (filtro.memoriaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("memoria"), filtro.memoriaMinima()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
