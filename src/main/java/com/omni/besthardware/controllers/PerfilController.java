package com.omni.besthardware.controllers;

import com.omni.besthardware.models.PerfilModel;
import com.omni.besthardware.services.PerfilService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/perfis")
public class PerfilController {

    private static final List<PerfilUsoResponse> PERFIS_DE_USO = List.of(
            new PerfilUsoResponse(
                    "trabalho",
                    "Trabalho",
                    "Navegador, pacote office, estudos, reunioes e tarefas leves.",
                    false,
                    "CPU economica com GPU integrada.",
                    "GPU dedicada nao obrigatoria.",
                    "8 GB a 16 GB.",
                    "SSD SATA ou NVME a partir de 256 GB.",
                    "Fonte 400 W a 500 W com certificacao basica.",
                    "Full HD, 60 Hz ou 75 Hz.",
                    List.of("navegador", "office", "aulas", "reunioes")
            ),
            new PerfilUsoResponse(
                    "jogo-inicial",
                    "Jogo inicial",
                    "Jogos em Full HD com GPU dedicada de entrada.",
                    true,
                    "CPU de 4 a 6 nucleos.",
                    "GPU dedicada de entrada com pelo menos 4 GB de VRAM.",
                    "16 GB.",
                    "SSD NVME a partir de 512 GB.",
                    "Fonte 500 W a 650 W.",
                    "Full HD, 75 Hz a 144 Hz.",
                    List.of("full hd", "esports", "jogos leves", "jogos medios")
            ),
            new PerfilUsoResponse(
                    "jogo-intermediario",
                    "Jogo intermediario",
                    "Jogos em 2K com boa qualidade grafica.",
                    true,
                    "CPU de 6 a 8 nucleos.",
                    "GPU dedicada intermediaria com 8 GB a 12 GB de VRAM.",
                    "16 GB a 32 GB.",
                    "SSD NVME a partir de 1 TB.",
                    "Fonte 650 W a 750 W.",
                    "2K, 144 Hz.",
                    List.of("2k", "qualidade alta", "jogos atuais")
            ),
            new PerfilUsoResponse(
                    "jogos-pesados",
                    "Jogos pesados",
                    "Altos FPS em 2K e jogos em 4K.",
                    true,
                    "CPU de 8 nucleos ou mais.",
                    "GPU dedicada forte com 12 GB ou mais de VRAM.",
                    "32 GB ou mais.",
                    "SSD NVME rapido a partir de 1 TB.",
                    "Fonte 750 W ou mais.",
                    "2K alto refresh ou 4K.",
                    List.of("alto fps", "2k ultra", "4k", "ray tracing")
            ),
            new PerfilUsoResponse(
                    "profissional",
                    "Profissional",
                    "SolidWorks, AutoCAD, renderizacao e aplicativos de alto desempenho.",
                    true,
                    "CPU de 8 nucleos ou mais, com alta frequencia.",
                    "GPU dedicada profissional ou gamer com bastante VRAM.",
                    "32 GB a 64 GB.",
                    "SSD NVME rapido a partir de 1 TB.",
                    "Fonte 750 W ou mais, conforme GPU escolhida.",
                    "2K ou 4K com boa fidelidade de imagem.",
                    List.of("solidworks", "autocad", "renderizacao", "modelagem 3d")
            )
    );

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    public List<PerfilModel> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer componenteId
    ) {
        if (nome != null) {
            return perfilService.buscarPorNome(nome);
        }

        if (componenteId != null) {
            return perfilService.buscarPorComponente(componenteId);
        }

        return perfilService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilModel> buscarPorId(@PathVariable Integer id) {
        return perfilService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PerfilModel> buscarPorNomeExato(@PathVariable String nome) {
        return perfilService.buscarPorNomeExato(nome)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/escopos")
    public List<PerfilUsoResponse> listarEscopos() {
        return PERFIS_DE_USO;
    }

    @GetMapping("/escopos/{codigo}")
    public ResponseEntity<PerfilUsoResponse> buscarEscopoPorCodigo(@PathVariable String codigo) {
        return PERFIS_DE_USO.stream()
                .filter(perfil -> perfil.codigo().equalsIgnoreCase(codigo))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PerfilModel> criar(@Valid @RequestBody PerfilModel perfil) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilService.salvar(perfil));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilModel> atualizar(@PathVariable Integer id, @Valid @RequestBody PerfilModel perfil) {
        return perfilService.atualizar(id, perfil)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        if (!perfilService.excluirPorId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    public record PerfilUsoResponse(
            String codigo,
            String nome,
            String objetivo,
            boolean exigeGpuDedicada,
            String processador,
            String gpu,
            String memoriaRam,
            String armazenamento,
            String fonte,
            String monitor,
            List<String> usosIndicados
    ) {
    }
}

