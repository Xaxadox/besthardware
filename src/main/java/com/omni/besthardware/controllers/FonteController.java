package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.FonteRequest;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.FonteModel;
import com.omni.besthardware.services.FonteService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fontes")
public class FonteController {

    private final FonteService fonteService;

    public FonteController(FonteService fonteService) {
        this.fonteService = fonteService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer potenciaMinima,
            @RequestParam(required = false) String certificacao
    ) {
        if (tipo != null) {
            return toResponseList(fonteService.buscarPorTipo(tipo));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(fonteService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        if (marca != null) {
            return toResponseList(fonteService.buscarPorMarca(marca));
        }

        if (potenciaMinima != null) {
            return toResponseList(fonteService.buscarPorPotenciaMinima(potenciaMinima));
        }

        if (certificacao != null) {
            return toResponseList(fonteService.buscarPorCertificacao(certificacao));
        }

        return toResponseList(fonteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return fonteService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody FonteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(fonteService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody FonteRequest request) {
        return fonteService.atualizar(id, toModel(request))
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!fonteService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<FonteModel> fontes) {
        return fontes.stream().map(DtoMapper::toComponenteResponse).toList();
    }

    private FonteModel toModel(FonteRequest request) {
        FonteModel fonte = new FonteModel();
        fonte.setTipo(request.tipo());
        fonte.setPreco(request.preco());
        fonte.setMarca(request.marca());
        fonte.setPotencia(request.potencia());
        fonte.setCertificacao(request.certificacao());
        return fonte;
    }
}
