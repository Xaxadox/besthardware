package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.RamModel;
import com.omni.besthardware.rest.dto.request.RamFiltroRequest;
import com.omni.besthardware.rest.dto.request.RamRequest;
import com.omni.besthardware.rest.dto.response.RamResponse;
import com.omni.besthardware.service.RamService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rams")
public class RamController {

    private final RamService ramService;
    private final ComponenteMapper componenteMapper;

    public RamController(RamService ramService, ComponenteMapper componenteMapper) {
        this.ramService = ramService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<RamResponse> listar(@ModelAttribute RamFiltroRequest filtro) {
        return ramService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toRamResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RamResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(componenteMapper.toRamResponse(ramService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<RamResponse> criar(@Valid @RequestBody RamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toRamResponse(ramService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RamResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody RamRequest request) {
        return ResponseEntity.ok(componenteMapper.toRamResponse(ramService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        ramService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
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
