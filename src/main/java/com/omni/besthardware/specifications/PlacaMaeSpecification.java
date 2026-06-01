package com.omni.besthardware.specifications;

import com.omni.besthardware.dtos.PlacaMaeFiltroRequest;
import com.omni.besthardware.models.PlacaMaeModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class PlacaMaeSpecification {

    private PlacaMaeSpecification() {
    }

    public static Specification<PlacaMaeModel> comFiltros(PlacaMaeFiltroRequest filtro) {
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

            if (filtro.socket() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("socket")), filtro.socket().toLowerCase()));
            }

            if (filtro.chipset() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("chipset")), filtro.chipset().toLowerCase()));
            }

            if (filtro.formato() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("formato")), filtro.formato().toLowerCase()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
