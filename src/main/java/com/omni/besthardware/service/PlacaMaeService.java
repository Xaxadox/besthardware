package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.request.PlacaMaeFiltroRequest;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import com.omni.besthardware.model.PlacaMaeModel;
import com.omni.besthardware.repository.PlacaMaeRepository;
import com.omni.besthardware.specifications.PlacaMaeSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servico responsavel pelas regras de negocio de PlacaMae no projeto BestHardware.
 */
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
    public PlacaMaeModel buscarObrigatorio(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Placa-mae", id));
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
    public PlacaMaeModel atualizarObrigatorio(Integer id, PlacaMaeModel placaMae) {
        if (!placaMaeRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Placa-mae", id);
        }

        placaMae.setId(id);
        return placaMaeRepository.save(placaMae);
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!placaMaeRepository.existsById(id)) {
            return false;
        }

        placaMaeRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Placa-mae", id);
        }
    }
}
