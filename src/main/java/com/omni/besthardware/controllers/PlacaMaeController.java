package com.omni.besthardware.controllers;

import com.omni.besthardware.dtos.ComponenteResponse;
import com.omni.besthardware.dtos.PlacaMaeRequest;
import com.omni.besthardware.exceptions.RecursoNaoEncontradoException;
import com.omni.besthardware.mappers.DtoMapper;
import com.omni.besthardware.models.PlacaMaeModel;
import com.omni.besthardware.services.PlacaMaeService;
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
@RequestMapping("/api/placas-mae")
public class PlacaMaeController {

    private final PlacaMaeService placaMaeService;

    public PlacaMaeController(PlacaMaeService placaMaeService) {
        this.placaMaeService = placaMaeService;
    }

    @GetMapping
    public List<ComponenteResponse> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal precoMinimo,
            @RequestParam(required = false) BigDecimal precoMaximo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String socket,
            @RequestParam(required = false) String chipset,
            @RequestParam(required = false) String formato
    ) {
        if (tipo != null) {
            return toResponseList(placaMaeService.buscarPorTipo(tipo));
        }

        if (precoMinimo != null && precoMaximo != null) {
            return toResponseList(placaMaeService.buscarPorFaixaDePreco(precoMinimo, precoMaximo));
        }

        if (marca != null) {
            return toResponseList(placaMaeService.buscarPorMarca(marca));
        }

        if (socket != null) {
            return toResponseList(placaMaeService.buscarPorSocket(socket));
        }

        if (chipset != null) {
            return toResponseList(placaMaeService.buscarPorChipset(chipset));
        }

        if (formato != null) {
            return toResponseList(placaMaeService.buscarPorFormato(formato));
        }

        return toResponseList(placaMaeService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponenteResponse> buscarPorId(@PathVariable Integer id) {
        return placaMaeService.buscarPorId(id)
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Placa-mae", id));
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> criar(@Valid @RequestBody PlacaMaeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DtoMapper.toComponenteResponse(placaMaeService.salvar(toModel(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponenteResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody PlacaMaeRequest request) {
        return placaMaeService.atualizar(id, toModel(request))
                .map(DtoMapper::toComponenteResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Placa-mae", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!placaMaeService.excluirPorId(id)) {
            throw new RecursoNaoEncontradoException("Placa-mae", id);
        }

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
