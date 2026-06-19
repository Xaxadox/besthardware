package com.omni.besthardware.service;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.omni.besthardware.exception.RecursoNaoEncontradoException;


public abstract class AbstractCrudService<T, ID> {

    private final JpaRepository<T, ID> repository;
    private final String nomeRecurso;
    private final BiConsumer<T, ID> idSetter;

    
    protected AbstractCrudService(JpaRepository<T, ID> repository, String nomeRecurso, BiConsumer<T, ID> idSetter) {
        this.repository = repository;
        this.nomeRecurso = nomeRecurso;
        this.idSetter = idSetter;
    }

    @Transactional(readOnly = true)
    public List<T> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<T> buscarPorId(ID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public T buscarObrigatorio(ID id) {
        return buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(nomeRecurso, id));
    }

    @Transactional
    public T salvar(T entidade) {
        return repository.save(entidade);
    }

    @Transactional
    public Optional<T> atualizar(ID id, T entidade) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }

        idSetter.accept(entidade, id);
        return Optional.of(repository.save(entidade));
    }

    @Transactional
    public T atualizarObrigatorio(ID id, T entidade) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException(nomeRecurso, id);
        }

        idSetter.accept(entidade, id);
        return repository.save(entidade);
    }

    @Transactional
    public boolean excluirPorId(ID id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    @Transactional
    public void excluirObrigatorio(ID id) {
        if (!excluirPorId(id)) {
            throw new RecursoNaoEncontradoException(nomeRecurso, id);
        }
    }
}
