package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ArmazenamentoRequest;
import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.ArmazenamentoModel;
import com.omni.besthardware.services.ArmazenamentoService;
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
@RequestMapping("/api/armazenamentos")
public class ArmazenamentoController {

    private final ArmazenamentoService armazenamentoService;

    public ArmazenamentoController(ArmazenamentoService armazenamentoService) {
        this.armazenamentoService = armazenamentoService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String tecnologia,
            @RequestParam(required = false) String padrao,
            @RequestParam(required = false) Integer memoriaMinima,
            @RequestParam(required = false) Integer velocidadeLeituraMinima,
            @RequestParam(required = false) Integer velocidadeEscritaMinima
    ) {
        if (tipo != null) {
            return toResponseList(armazenamentoService.buscarPorTipo(tipo));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(armazenamentoService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        if (tecnologia != null) {
            return toResponseList(armazenamentoService.buscarPorTecnologia(tecnologia));
        }

        if (padrao != null) {
            return toResponseList(armazenamentoService.buscarPorPadrao(padrao));
        }

        if (memoriaMinima != null) {
            return toResponseList(armazenamentoService.buscarPorMemoriaMinima(memoriaMinima));
        }

        if (velocidadeLeituraMinima != null) {
            return toResponseList(armazenamentoService.buscarPorVelocidadeLeituraMinima(velocidadeLeituraMinima));
        }

        if (velocidadeEscritaMinima != null) {
            return toResponseList(armazenamentoService.buscarPorVelocidadeEscritaMinima(velocidadeEscritaMinima));
        }

        return toResponseList(armazenamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return armazenamentoService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Armazenamento", id));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody ArmazenamentoRequest request) {
        ArmazenamentoModel armazenamento = toModel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(armazenamentoService.salvar(armazenamento)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ArmazenamentoRequest request
    ) {
        return armazenamentoService.atualizar(id, toModel(request))
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Armazenamento", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!armazenamentoService.excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Armazenamento", id);
        }

        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<ArmazenamentoModel> armazenamentos) {
        return armazenamentos.stream().map(DtoMapper::toComponenteResponse).toList();
    }

    private ArmazenamentoModel toModel(ArmazenamentoRequest request) {
        ArmazenamentoModel armazenamento = new ArmazenamentoModel();
        armazenamento.setTipo(request.tipo());
        armazenamento.setPreco(request.preco());
        armazenamento.setTecnologia(request.tecnologia());
        armazenamento.setPadrao(request.padrao());
        armazenamento.setVelocidadeEscrita(request.velocidadeEscrita());
        armazenamento.setVelocidadeLeitura(request.velocidadeLeitura());
        armazenamento.setMemoria(request.memoria());
        return armazenamento;
    }
}
