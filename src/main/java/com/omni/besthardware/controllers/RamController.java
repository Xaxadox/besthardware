package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.RamRequest;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.RamModel;
import com.omni.besthardware.services.RamService;
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
@RequestMapping("/api/rams")
public class RamController {

    private final RamService ramService;

    public RamController(RamService ramService) {
        this.ramService = ramService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String geracao,
            @RequestParam(required = false) Integer frequenciaMinima,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer memoriaMinima
    ) {
        if (tipo != null) {
            return toResponseList(ramService.buscarPorTipo(tipo));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(ramService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        if (geracao != null) {
            return toResponseList(ramService.buscarPorGeracao(geracao));
        }

        if (frequenciaMinima != null) {
            return toResponseList(ramService.buscarPorFrequenciaMinima(frequenciaMinima));
        }

        if (marca != null) {
            return toResponseList(ramService.buscarPorMarca(marca));
        }

        if (memoriaMinima != null) {
            return toResponseList(ramService.buscarPorMemoriaMinima(memoriaMinima));
        }

        return toResponseList(ramService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return ramService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("RAM", id));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody RamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(ramService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody RamRequest request) {
        return ramService.atualizar(id, toModel(request))
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("RAM", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!ramService.excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("RAM", id);
        }

        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<RamModel> rams) {
        return rams.stream().map(DtoMapper::toComponenteResponse).toList();
    }

    private RamModel toModel(RamRequest request) {
        RamModel ram = new RamModel();
        ram.setTipo(request.tipo());
        ram.setPreco(request.preco());
        ram.setGeracao(request.geracao());
        ram.setFrequencia(request.frequencia());
        ram.setMarca(request.marca());
        ram.setMemoria(request.memoria());
        return ram;
    }
}
