package com.omni.besthardware.rest.controller;

import com.omni.besthardware.rest.dto.response.ComponenteResponse;
import com.omni.besthardware.rest.dto.request.RamFiltroRequest;
import com.omni.besthardware.rest.dto.request.RamRequest;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.model.RamModel;
import com.omni.besthardware.service.RamService;
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

/**
 * Controlador REST responsavel pelas operacoes de Ram no projeto BestHardware.
 */
@RestController
@RequestMapping("/api/rams")
public class RamController {

    private final RamService ramService;

    public RamController(RamService ramService) {
        this.ramService = ramService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(@ModelAttribute RamFiltroRequest filtro) {
        return toResponseList(ramService.buscarComFiltros(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(ramService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody RamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(ramService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody RamRequest request) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(ramService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        ramService.excluirObrigatorio(id);
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
