package com.omni.besthardware.services;

import com.omni.besthardware.models.RamModel;
import com.omni.besthardware.repositories.RamRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RamService {

    private final RamRepository ramRepository;

    public RamService(RamRepository ramRepository) {
        this.ramRepository = ramRepository;
    }

    @Transactional(readOnly = true)
    public List<RamModel> listarTodos() {
        return ramRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RamModel> buscarPorId(Integer id) {
        return ramRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorTipo(String tipo) {
        return ramRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return ramRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorGeracao(String geracao) {
        return ramRepository.findByGeracaoIgnoreCase(geracao);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorFrequenciaMinima(Integer frequencia) {
        return ramRepository.findByFrequenciaGreaterThanEqual(frequencia);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorMarca(String marca) {
        return ramRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Transactional(readOnly = true)
    public List<RamModel> buscarPorMemoriaMinima(Integer memoria) {
        return ramRepository.findByMemoriaGreaterThanEqual(memoria);
    }

    @Transactional
    public RamModel salvar(RamModel ram) {
        return ramRepository.save(ram);
    }

    @Transactional
    public Optional<RamModel> atualizar(Integer id, RamModel ram) {
        if (!ramRepository.existsById(id)) {
            return Optional.empty();
        }

        ram.setId(id);
        return Optional.of(ramRepository.save(ram));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!ramRepository.existsById(id)) {
            return false;
        }

        ramRepository.deleteById(id);
        return true;
    }
}
