package com.omni.besthardware.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RecursoNaoEncontradoException(String recurso, Object identificador) {
        super(recurso + " nao encontrado para identificador " + identificador + ".");
    }
}
