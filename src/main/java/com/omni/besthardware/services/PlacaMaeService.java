package com.omni.besthardware.services;

import com.omni.besthardware.dtos.PlacaMaeFiltroRequest;
import com.omni.besthardware.models.PlacaMaeModel;
import com.omni.besthardware.repositories.PlacaMaeRepository;
import com.omni.besthardware.specifications.PlacaMaeSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlacaMaeService {

    private final PlacaMaeRepository placaMaeRepository;

    public PlacaMaeService(PlacaMaeRepository placaMaeRepository) {
        this.placaMaeRepository = placaMaeRepository;
    }

    @Transactional(readOnly = true)
    public List<PlacaMaeModel> listarTodos() {
        return placaMaeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PlacaMaeModel> buscarPorId(Integer id) {
        return placaMaeRepository.findById(id);
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

    @Transactional
    public PlacaMaeModel salvar(PlacaMaeModel placaMae) {
        return placaMaeRepository.save(placaMae);
    }

    @Transactional
    public Optional<PlacaMaeModel> atualizar(Integer id, PlacaMaeModel placaMae) {
        if (!placaMaeRepository.existsById(id)) {
            return Optional.empty();
        }

        placaMae.setId(id);
        return Optional.of(placaMaeRepository.save(placaMae));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!placaMaeRepository.existsById(id)) {
            return false;
        }

        placaMaeRepository.deleteById(id);
        return true;
    }
}
