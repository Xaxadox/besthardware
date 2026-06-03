package com.omni.besthardware.specifications;

import com.omni.besthardware.rest.dto.request.GpuFiltroRequest;
import com.omni.besthardware.model.GpuModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class GpuSpecification {

    private GpuSpecification() {
    }

    public static Specification<GpuModel> comFiltros(GpuFiltroRequest filtro) {
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

            if (filtro.modelo() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("modelo")), "%" + filtro.modelo().toLowerCase() + "%"));
            }

            if (filtro.marca() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + filtro.marca().toLowerCase() + "%"));
            }

            if (filtro.memoriaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("memoria"), filtro.memoriaMinima()));
            }

            if (filtro.consumoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("consumo"), filtro.consumoMaximo()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
