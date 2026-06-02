package com.omni.besthardware.specifications;

import com.omni.besthardware.dtos.OfertaPrecoFiltroRequest;
import com.omni.besthardware.models.OfertaPrecoModel;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class OfertaPrecoSpecification {

    private OfertaPrecoSpecification() {
    }

    public static Specification<OfertaPrecoModel> comFiltros(OfertaPrecoFiltroRequest filtro) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.componenteId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("componente").get("id"), filtro.componenteId()));
            }

            if (filtro.loja() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("loja")),
                        "%" + filtro.loja().toLowerCase() + "%"
                ));
            }

            if (filtro.precoAvistaMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("precoAvista"), filtro.precoAvistaMinimo()));
            }

            if (filtro.precoAvistaMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precoAvista"), filtro.precoAvistaMaximo()));
            }

            if (filtro.precoParceladoMinimo() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("precoParcelado"), filtro.precoParceladoMinimo()));
            }

            if (filtro.precoParceladoMaximo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("precoParcelado"), filtro.precoParceladoMaximo()));
            }

            if (filtro.parcelasMaximas() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("parcelas"), filtro.parcelasMaximas()));
            }

            if (filtro.cupom() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("cupom")),
                        "%" + filtro.cupom().toLowerCase() + "%"
                ));
            }

            if (filtro.fonte() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("fonte")),
                        "%" + filtro.fonte().toLowerCase() + "%"
                ));
            }

            if (filtro.dataInicial() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataColeta"), filtro.dataInicial()));
            }

            if (filtro.dataFinal() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataColeta"), filtro.dataFinal()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
