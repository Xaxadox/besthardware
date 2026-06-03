package com.omni.besthardware.specifications;

import com.omni.besthardware.rest.dto.request.ArmazenamentoFiltroRequest;
import com.omni.besthardware.model.ArmazenamentoModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ArmazenamentoSpecification {

    private ArmazenamentoSpecification() {
    }

    public static Specification<ArmazenamentoModel> comFiltros(ArmazenamentoFiltroRequest filtro) {
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

            if (filtro.tecnologia() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("tecnologia")), filtro.tecnologia().toLowerCase()));
            }

            if (filtro.padrao() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("padrao")), filtro.padrao().toLowerCase()));
            }

            if (filtro.memoriaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("memoria"), filtro.memoriaMinima()));
            }

            if (filtro.velocidadeLeituraMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("velocidadeLeitura"), filtro.velocidadeLeituraMinima()));
            }

            if (filtro.velocidadeEscritaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("velocidadeEscrita"), filtro.velocidadeEscritaMinima()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
