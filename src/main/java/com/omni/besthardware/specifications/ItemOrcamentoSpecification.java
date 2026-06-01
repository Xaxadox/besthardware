package com.omni.besthardware.specifications;

import com.omni.besthardware.dtos.ItemOrcamentoFiltroRequest;
import com.omni.besthardware.models.ItemOrcamentoModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ItemOrcamentoSpecification {

    private ItemOrcamentoSpecification() {
    }

    public static Specification<ItemOrcamentoModel> comFiltros(ItemOrcamentoFiltroRequest filtro) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.orcamentoId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("orcamento").get("id"), filtro.orcamentoId()));
            }

            if (filtro.componenteId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("componente").get("id"), filtro.componenteId()));
            }

            if (filtro.quantidadeMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantidade"), filtro.quantidadeMinima()));
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
