package com.omni.besthardware.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omni.besthardware.model.ComponenteModel;
import com.omni.besthardware.repository.ComponenteRepository;
import com.omni.besthardware.rest.dto.request.ComponenteFiltroRequest;
import com.omni.besthardware.specifications.ComponenteSpecification;


@Service
public class ComponenteService extends AbstractCrudService<ComponenteModel, Integer> {

    private final ComponenteRepository componenteRepository;

    public ComponenteService(ComponenteRepository componenteRepository) {
        super(componenteRepository, "Componente", ComponenteModel::setId);
        this.componenteRepository = componenteRepository;
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarComFiltros(ComponenteFiltroRequest filtro) {
        return componenteRepository.findAll(ComponenteSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarPorTipo(String tipo) {
        return componenteRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return componenteRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarPorPerfil(Integer perfilId) {
        return componenteRepository.findByPerfisId(perfilId);
    }

    @Transactional(readOnly = true)
    public List<ComponenteModel> buscarCompativeisCom(Integer componenteCompativelId) {
        return componenteRepository.findByComponentesCompativeisId(componenteCompativelId);
    }
}
