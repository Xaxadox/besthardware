package com.omni.besthardware.specifications;

import com.omni.besthardware.rest.dto.request.CpuFiltroRequest;
import com.omni.besthardware.model.CpuModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class CpuSpecification {

    private CpuSpecification() {
    }

    public static Specification<CpuModel> comFiltros(CpuFiltroRequest filtro) {
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

            if (filtro.modelo() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("modelo")),
                        "%" + filtro.modelo().toLowerCase() + "%"
                ));
            }

            if (filtro.socket() != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("socket")),
                        filtro.socket().toLowerCase()
                ));
            }

            if (filtro.frequenciaMinima() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("frequencia"), filtro.frequenciaMinima()));
            }

            if (filtro.consumoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("consumo"), filtro.consumoMaximo()));
            }

            if (filtro.nucleosMinimos() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("nucleos"), filtro.nucleosMinimos()));
            }

            if (filtro.dataInicial() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("anoLancamento"), filtro.dataInicial()));
            }

            if (filtro.dataFinal() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("anoLancamento"), filtro.dataFinal()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
