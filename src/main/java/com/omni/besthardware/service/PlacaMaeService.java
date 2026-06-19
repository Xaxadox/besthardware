package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.PlacaMaeFiltroRequest;
import com.omni.besthardware.model.PlacaMaeModel;
import com.omni.besthardware.repository.PlacaMaeRepository;
import com.omni.besthardware.specifications.PlacaMaeSpecification;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PlacaMaeService extends AbstractCrudService<PlacaMaeModel, Integer> {

    private final PlacaMaeRepository placaMaeRepository;

    public PlacaMaeService(PlacaMaeRepository placaMaeRepository) {
        super(placaMaeRepository, "Placa-mae", PlacaMaeModel::setId);
        this.placaMaeRepository = placaMaeRepository;
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> buscarComFiltros(PlacaMaeFiltroRequest filtro) {
        return placaMaeRepository.findAll(PlacaMaeSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> buscarPorTipo(String tipo) {
        return placaMaeRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return placaMaeRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> buscarPorMarca(String marca) {
        return placaMaeRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> buscarPorSocket(String socket) {
        return placaMaeRepository.findBySocketIgnoreCase(socket);
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> buscarPorChipset(String chipset) {
        return placaMaeRepository.findByChipsetIgnoreCase(chipset);
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> buscarPorFormato(String formato) {
        return placaMaeRepository.findByFormatoIgnoreCase(formato);
    }
}
