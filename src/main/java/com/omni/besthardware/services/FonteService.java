package com.omni.besthardware.services;

import com.omni.besthardware.dtos.FonteFiltroRequest;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.models.FonteModel;
import com.omni.besthardware.repositories.FonteRepository;
import com.omni.besthardware.specifications.FonteSpecification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FonteService {

    private final FonteRepository fonteRepository;

    public FonteService(FonteRepository fonteRepository) {
        this.fonteRepository = fonteRepository;
    }

    @Transactional(readOnly = true)
    public List<FonteModel> listarTodos() {
        return fonteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<FonteModel> buscarPorId(Integer id) {
        return fonteRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public FonteModel buscarObrigatorio(Integer id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fonte", id));
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarComFiltros(FonteFiltroRequest filtro) {
        return fonteRepository.findAll(FonteSpecification.comFiltros(filtro));
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorTipo(String tipo) {
        return fonteRepository.findByTipoIgnoreCase(tipo);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorFaixaDePreco(BigDecimal precoMinimo, BigDecimal precoMaximo) {
        return fonteRepository.findByPrecoBetween(precoMinimo, precoMaximo);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorMarca(String marca) {
        return fonteRepository.findByMarcaContainingIgnoreCase(marca);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorPotenciaMinima(Integer potencia) {
        return fonteRepository.findByPotenciaGreaterThanEqual(potencia);
    }

    @Transactional(readOnly = true)
    public List<FonteModel> buscarPorCertificacao(String certificacao) {
        return fonteRepository.findByCertificacaoContainingIgnoreCase(certificacao);
    }

    @Transactional
    public FonteModel salvar(FonteModel fonte) {
        return fonteRepository.save(fonte);
    }

    @Transactional
    public Optional<FonteModel> atualizar(Integer id, FonteModel fonte) {
        if (!fonteRepository.existsById(id)) {
            return Optional.empty();
        }

        fonte.setId(id);
        return Optional.of(fonteRepository.save(fonte));
    }

    @Transactional
    public FonteModel atualizarObrigatorio(Integer id, FonteModel fonte) {
        if (!fonteRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Fonte", id);
        }

        fonte.setId(id);
        return fonteRepository.save(fonte);
    }

    @Transactional
    public boolean excluirPorId(Integer id) {
        if (!fonteRepository.existsById(id)) {
            return false;
        }

        fonteRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(Integer id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Fonte", id);
        }
    }
}
