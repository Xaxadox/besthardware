package com.omni.besthardware.service;

import com.omni.besthardware.rest.dto.response.PerfilUsoResponse;
import com.omni.besthardware.exception.RecursoNaoEncontradoException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public class PerfilUsoService {

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

    public List<PerfilUsoResponse> listarTodos() {
        return PERFIS_DE_USO;
    }

    public Optional<PerfilUsoResponse> buscarPorCodigo(String codigo) {
        return PERFIS_DE_USO.stream()
                .filter(perfil -> perfil.codigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    public PerfilUsoResponse buscarPorCodigoObrigatorio(String codigo) {
        return buscarPorCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Escopo de perfil nao encontrado: " + codigo + "."));
    }
}
