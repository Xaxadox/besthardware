package com.omni.besthardware.dtos;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        List<String> detalhes
) {

    public static ErroResponse of(HttpStatus status, String mensagem, String caminho) {
        return of(status, mensagem, caminho, List.of());
    }

    public static ErroResponse of(HttpStatus status, String mensagem, String caminho, List<String> detalhes) {
        return new ErroResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                caminho,
                detalhes
        );
    }
}
