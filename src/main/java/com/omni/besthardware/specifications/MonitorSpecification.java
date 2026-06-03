package com.omni.besthardware.specifications;

import com.omni.besthardware.rest.dto.request.MonitorFiltroRequest;
import com.omni.besthardware.model.MonitorModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class MonitorSpecification {

    private MonitorSpecification() {
    }

    public static Specification<MonitorModel> comFiltros(MonitorFiltroRequest filtro) {
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

            if (filtro.tamanho() != null) {
                predicates.add(criteriaBuilder.equal(root.get("tamanho"), filtro.tamanho()));
            }

            if (filtro.resolucao() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("resolucao")), filtro.resolucao().toLowerCase()));
            }

            if (filtro.frequenciaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("frequencia"), filtro.frequenciaMinima()));
            }

            if (filtro.tecnologia() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("tecnologia")), filtro.tecnologia().toLowerCase()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
