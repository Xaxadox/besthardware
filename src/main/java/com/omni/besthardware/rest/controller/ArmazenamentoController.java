package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.ArmazenamentoModel;
import com.omni.besthardware.rest.dto.request.ArmazenamentoFiltroRequest;
import com.omni.besthardware.rest.dto.request.ArmazenamentoRequest;
import com.omni.besthardware.rest.dto.response.ArmazenamentoResponse;
import com.omni.besthardware.service.ArmazenamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/armazenamentos")
public class ArmazenamentoController {

    private final ArmazenamentoService armazenamentoService;
    private final ComponenteMapper componenteMapper;

    public ArmazenamentoController(ArmazenamentoService armazenamentoService, ComponenteMapper componenteMapper) {
        this.armazenamentoService = armazenamentoService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<ArmazenamentoResponse> listar(@Valid @ModelAttribute ArmazenamentoFiltroRequest filtro) {
        return armazenamentoService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toArmazenamentoResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArmazenamentoResponse> buscarPorId(@PathVariable @Positive Integer id) {
        return ResponseEntity.ok(componenteMapper.toArmazenamentoResponse(armazenamentoService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<ArmazenamentoResponse> criar(@Valid @RequestBody ArmazenamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toArmazenamentoResponse(armazenamentoService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArmazenamentoResponse> atualizar(
            @PathVariable @Positive Integer id,
            @Valid @RequestBody ArmazenamentoRequest request
    ) {
        return ResponseEntity.ok(componenteMapper.toArmazenamentoResponse(
                armazenamentoService.atualizarObrigatorio(id, toModel(request))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive Integer id) {
        armazenamentoService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
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
