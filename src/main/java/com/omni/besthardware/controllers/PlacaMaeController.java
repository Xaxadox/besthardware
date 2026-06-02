package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.PlacaMaeFiltroRequest;
import com.omni.besthardware.dtos.PlacaMaeRequest;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.PlacaMaeModel;
import com.omni.besthardware.services.PlacaMaeService;
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
@RequestMapping("/api/placas-mae")
public class PlacaMaeController {

    private final PlacaMaeService placaMaeService;

    public PlacaMaeController(PlacaMaeService placaMaeService) {
        this.placaMaeService = placaMaeService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(@ModelAttribute PlacaMaeFiltroRequest filtro) {
        return toResponseList(placaMaeService.buscarComFiltros(filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(placaMaeService.buscarObrigatorio(id)));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody PlacaMaeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(placaMaeService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody PlacaMaeRequest request) {
        return ResponseEntity.ok(DtoMapper.toComponenteResponse(placaMaeService.atualizarObrigatorio(id, toModel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        placaMaeService.excluirObrigatorio(id);
        return ResponseEntity.noContent().build();
    }

    private List<ComponenteResponse> toResponseList(List<PlacaMaeModel> placasMae) {
        return placasMae.stream().map(DtoMapper::toComponenteResponse).toList();
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
