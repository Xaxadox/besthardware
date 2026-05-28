package com.omni.besthardware.services;

import com.omni.besthardware.models.OrcamentoModel;
import com.omni.besthardware.repositories.OrcamentoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> listarTodos() {
        return orcamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<OrcamentoModel> buscarPorId(Integer id) {
        return orcamentoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorNome(String nome) {
        return orcamentoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorUsuario(Integer usuarioId) {
        return orcamentoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorPerfil(Integer perfilId) {
        return orcamentoRepository.findByPerfilId(perfilId);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorPeriodoCriacao(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return orcamentoRepository.findByDataCriacaoBetween(dataInicial, dataFinal);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return orcamentoRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional
    public OrcamentoModel salvar(OrcamentoModel orcamento) {
        return orcamentoRepository.save(orcamento);
    }

    @Transactional
    public Optional<OrcamentoModel> atualizar(Integer id, OrcamentoModel orcamento) {
        if (!orcamentoRepository.existsById(id)) {
            return Optional.empty();
        }

        orcamento.setId(id);
        return Optional.of(orcamentoRepository.save(orcamento));
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!orcamentoRepository.existsById(id)) {
            return false;
        }

        orcamentoRepository.deleteById(id);
        return true;
    }
}
