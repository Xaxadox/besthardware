package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.PlacaMaeModel;
import com.omni.besthardware.rest.dto.request.PlacaMaeFiltroRequest;
import com.omni.besthardware.rest.dto.request.PlacaMaeRequest;
import com.omni.besthardware.rest.dto.response.PlacaMaeResponse;
import com.omni.besthardware.service.PlacaMaeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/placas-mae")
public class PlacaMaeController {

    private final PlacaMaeService placaMaeService;
    private final ComponenteMapper componenteMapper;

    public PlacaMaeController(PlacaMaeService placaMaeService, ComponenteMapper componenteMapper) {
        this.placaMaeService = placaMaeService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<PlacaMaeResponse> listar(@Valid @ModelAttribute PlacaMaeFiltroRequest filtro) {
        return placaMaeService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toPlacaMaeResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacaMaeResponse> buscarPorId(@PathVariable @Positive Integer id) {
        return ResponseEntity.ok(componenteMapper.toPlacaMaeResponse(placaMaeService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<PlacaMaeResponse> criar(@Valid @RequestBody PlacaMaeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toPlacaMaeResponse(placaMaeService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlacaMaeResponse> atualizar(@PathVariable @Positive Integer id, @Valid @RequestBody PlacaMaeRequest request) {
        return ResponseEntity.ok(componenteMapper.toPlacaMaeResponse(placaMaeService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive Integer id) {
        placaMaeService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private PlacaMaeModel toModel(PlacaMaeRequest request) {
        PlacaMaeModel placaMae = new PlacaMaeModel();
        placaMae.setTipo(request.tipo());
        placaMae.setPreco(request.preco());
        placaMae.setMarca(request.marca());
        placaMae.setSocket(request.socket());
        placaMae.setChipset(request.chipset());
        placaMae.setFormato(request.formato());
        return placaMae;
    }
}
