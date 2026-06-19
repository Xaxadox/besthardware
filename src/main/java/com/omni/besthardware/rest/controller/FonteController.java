package com.omni.besthardware.rest.controller;

import com.omni.besthardware.mappers.ComponenteMapper;
import com.omni.besthardware.model.FonteModel;
import com.omni.besthardware.rest.dto.request.FonteFiltroRequest;
import com.omni.besthardware.rest.dto.request.FonteRequest;
import com.omni.besthardware.rest.dto.response.FonteResponse;
import com.omni.besthardware.service.FonteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fontes")
public class FonteController {

    private final FonteService fonteService;
    private final ComponenteMapper componenteMapper;

    public FonteController(FonteService fonteService, ComponenteMapper componenteMapper) {
        this.fonteService = fonteService;
        this.componenteMapper = componenteMapper;
    }

    @GetMapping
    public List<FonteResponse> listar(@ModelAttribute FonteFiltroRequest filtro) {
        return fonteService.buscarComFiltros(filtro).stream()
                .map(componenteMapper::toFonteResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FonteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(componenteMapper.toFonteResponse(fonteService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<FonteResponse> criar(@Valid @RequestBody FonteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(componenteMapper.toFonteResponse(fonteService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FonteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody FonteRequest request) {
        return ResponseEntity.ok(componenteMapper.toFonteResponse(fonteService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        fonteService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
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
