package com.omni.besthardware.handlers;

import com.omni.besthardware.dtos.ErroResponse;
import com.omni.besthardware.exceptions.ConflitoException;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.exceptions.ValidacaoNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception,
            HttpServletRequest request
    ) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(ValidacaoNegocioException.class)
    public ResponseEntity<ErroResponse> tratarValidacaoNegocio(
            ValidacaoNegocioException exception,
            HttpServletRequest request
    ) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponse> tratarConflito(
            ConflitoException exception,
            HttpServletRequest request
    ) {
        return build(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarArgumentosInvalidos(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<String> detalhes = exception.getBindingResult().getFieldErrors().stream()
                .map(this::toDetalhe)
                .distinct()
                .toList();

        return build(
                HttpStatus.BAD_REQUEST,
                "Existem campos invalidos na requisicao.",
                request,
                detalhes
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> tratarViolacaoDeRestricao(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        List<String> detalhes = exception.getConstraintViolations().stream()
                .map(violacao -> violacao.getPropertyPath() + ": " + violacao.getMessage())
                .distinct()
                .toList();

        return build(
                HttpStatus.BAD_REQUEST,
                "Existem parametros invalidos na requisicao.",
                request,
                detalhes
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarTipoInvalido(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        String tipoEsperado = exception.getRequiredType() == null
                ? "tipo esperado"
                : exception.getRequiredType().getSimpleName();
        String mensagem = "Parametro '" + exception.getName() + "' recebeu valor invalido '"
                + exception.getValue() + "'. Tipo esperado: " + tipoEsperado + ".";

        return build(HttpStatus.BAD_REQUEST, mensagem, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> tratarParametroObrigatorioAusente(
            MissingServletRequestParameterException exception,
            HttpServletRequest request
    ) {
        String mensagem = "Parametro obrigatorio '" + exception.getParameterName() + "' nao informado.";
        return build(HttpStatus.BAD_REQUEST, mensagem, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                "Corpo da requisicao esta invalido ou mal formatado.",
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarViolacaoDeIntegridade(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.CONFLICT,
                "Operacao nao pode ser concluida porque o registro possui vinculos ou viola uma regra do banco.",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroInesperado(Exception exception, HttpServletRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno inesperado. Verifique os dados enviados ou consulte os logs da aplicacao.",
                request
        );
    }

    private String toDetalhe(FieldError erro) {
        return erro.getField() + ": " + Objects.requireNonNullElse(erro.getDefaultMessage(), "valor invalido");
    }

    private ResponseEntity<ErroResponse> build(HttpStatus status, String mensagem, HttpServletRequest request) {
        return build(status, mensagem, request, List.of());
    }

    private ResponseEntity<ErroResponse> build(
            HttpStatus status,
            String mensagem,
            HttpServletRequest request,
            List<String> detalhes
    ) {
        return ResponseEntity.status(status)
                .body(ErroResponse.of(status, mensagem, request.getRequestURI(), detalhes));
    }
}
