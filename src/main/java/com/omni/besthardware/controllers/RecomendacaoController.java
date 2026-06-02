package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.CompatibilidadeRequest;
import com.omni.besthardware.dtos.CompatibilidadeResponse;
import com.omni.besthardware.dtos.RecomendacaoResponse;
import com.omni.besthardware.services.CompatibilidadeService;
import com.omni.besthardware.services.RecomendacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;
    private final CompatibilidadeService compatibilidadeService;

    public RecomendacaoController(
            RecomendacaoService recomendacaoService,
            CompatibilidadeService compatibilidadeService
    ) {
        this.recomendacaoService = recomendacaoService;
        this.compatibilidadeService = compatibilidadeService;
    }

    @GetMapping("/perfis")
    public List<RecomendacaoResponse> listarRecomendacoes() {
        return recomendacaoService.listarTodas();
    }

    @GetMapping("/perfis/{codigoPerfil}")
    public ResponseEntity<RecomendacaoResponse> recomendarPorPerfil(@PathVariable String codigoPerfil) {
        return ResponseEntity.ok(recomendacaoService.recomendarObrigatorio(codigoPerfil));
    }

    @PostMapping("/compatibilidade")
    public ResponseEntity<CompatibilidadeResponse> verificarCompatibilidade(
            @Valid @RequestBody CompatibilidadeRequest request
    ) {
        return ResponseEntity.ok(compatibilidadeService.verificarPorIdsObrigatorio(request.componenteIds()));
    }
}
