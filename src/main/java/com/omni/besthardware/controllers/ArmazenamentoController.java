package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ArmazenamentoFiltroRequest;
import com.omni.besthardware.dtos.ArmazenamentoRequest;
import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.ArmazenamentoModel;
import com.omni.besthardware.services.ArmazenamentoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/armazenamentos")
public class ArmazenamentoController {

    private final ArmazenamentoService armazenamentoService;

    public ArmazenamentoController(ArmazenamentoService armazenamentoService) {
        this.armazenamentoService = armazenamentoService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(@ModelAttribute ArmazenamentoFiltroRequest filtro) {
        return toResponseList(armazenamentoService.buscarComFiltros(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(armazenamentoService.buscarObrigatorio(id)));
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
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(
                armazenamentoService.atualizarObrigatorio(id, toModel(request))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        armazenamentoService.excluirObrigatorio(id);
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
