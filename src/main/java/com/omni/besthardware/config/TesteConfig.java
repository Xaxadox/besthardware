package com.omni.besthardware.config;

import com.omni.besthardware.models.*;
import com.omni.besthardware.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class TesteConfig implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private CpuRepository cpuRepository;

    @Autowired
    private PlacaMaeRepository placaMaeRepository;

    @Autowired
    private GpuRepository gpuRepository;

    @Autowired
    private RamRepository ramRepository;

    @Autowired
    private FonteRepository fonteRepository;

    @Autowired
    private ArmazenamentoRepository armazenamentoRepository;

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private ItemOrcamentoRepository itemOrcamentoRepository;

    @Override
    public void run(String... args) throws Exception {

        // Evita duplicar dados se a aplicação reiniciar
        if (usuarioRepository.count() > 0) {
            return;
        }

        // =========================
        // USUÁRIOS
        // =========================
        UsuarioModel usuario1 = new UsuarioModel();
        usuario1.setNome("Caio");
        usuario1.setEmail("caio@email.com");

        UsuarioModel usuario2 = new UsuarioModel();
        usuario2.setNome("Agnaldo");
        usuario2.setEmail("agnaldo@email.com");

        usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2));

        // =========================
        // PERFIS
        // =========================
        PerfilModel perfilGamer = new PerfilModel();
        perfilGamer.setNome("Gamer");

        PerfilModel perfilTrabalho = new PerfilModel();
        perfilTrabalho.setNome("Trabalho");

        perfilRepository.saveAll(Arrays.asList(perfilGamer, perfilTrabalho));

        // =========================
        // COMPONENTES
        // =========================

        CpuModel cpu = new CpuModel();
        cpu.setTipo("CPU");
        cpu.setPreco(new BigDecimal("899.90"));
        cpu.setModelo("Ryzen 5 5600");
        cpu.setFrequencia(3500);
        cpu.setConsumo(65);
        cpu.setAnoLancamento(LocalDate.of(2022, 4, 4));
        cpu.setNucleos(6);
        cpu.setSocket("AM4");

        PlacaMaeModel placaMae = new PlacaMaeModel();
        placaMae.setTipo("PlacaMae");
        placaMae.setPreco(new BigDecimal("699.90"));
        placaMae.setMarca("ASUS");
        placaMae.setSocket("AM4");
        placaMae.setChipset("B550");
        placaMae.setFormato("ATX");

        GpuModel gpu = new GpuModel();
        gpu.setTipo("GPU");
        gpu.setPreco(new BigDecimal("1899.90"));
        gpu.setModelo("RTX 3060");
        gpu.setMemoria(12);
        gpu.setConsumo(170);
        gpu.setMarca("NVIDIA");

        RamModel ram = new RamModel();
        ram.setTipo("ram");
        ram.setGeracao("DDR4");
        ram.setPreco(new BigDecimal("449.90"));
        ram.setFrequencia(3200);
        ram.setMarca("Kingston");
        ram.setMemoria(8);

        FonteModel fonte = new FonteModel();
        fonte.setTipo("Fonte");
        fonte.setPreco(new BigDecimal("399.90"));
        fonte.setMarca("Corsair");
        fonte.setPotencia(650);
        fonte.setCertificacao("80 Plus Bronze");

        ArmazenamentoModel armazenamento = new ArmazenamentoModel();
        armazenamento.setTipo("Armazenamento");
        armazenamento.setTecnologia("SSD");
        armazenamento.setPreco(new BigDecimal("299.90"));
        armazenamento.setPadrao("NVME");
        armazenamento.setVelocidadeEscrita(3000);
        armazenamento.setVelocidadeLeitura(3500);
        armazenamento.setMemoria(256);

        MonitorModel monitor = new MonitorModel();
        monitor.setTipo("Monitor");
        monitor.setTecnologia("IPS");
        monitor.setPreco(new BigDecimal("899.90"));
        monitor.setMarca("LG");
        monitor.setTamanho("24");
        monitor.setResolucao("1920x1080");
        monitor.setFrequencia(144);

        cpuRepository.save(cpu);
        placaMaeRepository.save(placaMae);
        gpuRepository.save(gpu);
        ramRepository.save(ram);
        fonteRepository.save(fonte);
        armazenamentoRepository.save(armazenamento);
        monitorRepository.save(monitor);

        // =========================
        // N:N 1 - PERFIL ↔ COMPONENTE
        // =========================
        perfilGamer.getComponentes().add(cpu);
        perfilGamer.getComponentes().add(placaMae);
        perfilGamer.getComponentes().add(gpu);
        perfilGamer.getComponentes().add(ram);
        perfilGamer.getComponentes().add(fonte);
        perfilGamer.getComponentes().add(armazenamento);
        perfilGamer.getComponentes().add(monitor);

        perfilTrabalho.getComponentes().add(cpu);
        perfilTrabalho.getComponentes().add(placaMae);
        perfilTrabalho.getComponentes().add(ram);
        perfilTrabalho.getComponentes().add(armazenamento);
        perfilTrabalho.getComponentes().add(monitor);

        perfilRepository.saveAll(Arrays.asList(perfilGamer, perfilTrabalho));

        // =========================
        // N:N 2 - COMPONENTE ↔ COMPONENTE
        // Compatibilidade entre peças
        // =========================

        // CPU AM4 compatível com placa-mãe AM4
        cpu.getComponentesCompativeis().add(placaMae);
        placaMae.getComponentesCompativeis().add(cpu);

        // Placa-mãe compatível com RAM DDR4
        placaMae.getComponentesCompativeis().add(ram);
        ram.getComponentesCompativeis().add(placaMae);

        // GPU compatível com fonte de 650W
        gpu.getComponentesCompativeis().add(fonte);
        fonte.getComponentesCompativeis().add(gpu);

        // SSD NVME compatível com placa-mãe
        armazenamento.getComponentesCompativeis().add(placaMae);
        placaMae.getComponentesCompativeis().add(armazenamento);

        cpuRepository.save(cpu);
        placaMaeRepository.save(placaMae);
        gpuRepository.save(gpu);
        ramRepository.save(ram);
        fonteRepository.save(fonte);
        armazenamentoRepository.save(armazenamento);

        // =========================
        // ORÇAMENTO
        // =========================
        OrcamentoModel orcamento = new OrcamentoModel();
        orcamento.setNome("PC Gamer AM4");
        orcamento.setDataCriacao(LocalDateTime.now());
        orcamento.setUsuario(usuario1);
        orcamento.setPerfil(perfilGamer);
        orcamento.setPreco(new BigDecimal("5349.40"));

        orcamentoRepository.save(orcamento);

        // =========================
        // ITENS DO ORÇAMENTO
        // =========================
        ItemOrcamentoModel itemCpu = new ItemOrcamentoModel();
        itemCpu.setQuantidade(1);
        itemCpu.setPreco(cpu.getPreco());
        itemCpu.setOrcamento(orcamento);
        itemCpu.setComponente(cpu);

        ItemOrcamentoModel itemPlacaMae = new ItemOrcamentoModel();
        itemPlacaMae.setQuantidade(1);
        itemPlacaMae.setPreco(placaMae.getPreco());
        itemPlacaMae.setOrcamento(orcamento);
        itemPlacaMae.setComponente(placaMae);

        ItemOrcamentoModel itemGpu = new ItemOrcamentoModel();
        itemGpu.setQuantidade(1);
        itemGpu.setPreco(gpu.getPreco());
        itemGpu.setOrcamento(orcamento);
        itemGpu.setComponente(gpu);

        ItemOrcamentoModel itemRam = new ItemOrcamentoModel();
        itemRam.setQuantidade(2);
        itemRam.setPreco(ram.getPreco());
        itemRam.setOrcamento(orcamento);
        itemRam.setComponente(ram);

        ItemOrcamentoModel itemFonte = new ItemOrcamentoModel();
        itemFonte.setQuantidade(1);
        itemFonte.setPreco(fonte.getPreco());
        itemFonte.setOrcamento(orcamento);
        itemFonte.setComponente(fonte);

        ItemOrcamentoModel itemArmazenamento = new ItemOrcamentoModel();
        itemArmazenamento.setQuantidade(1);
        itemArmazenamento.setPreco(armazenamento.getPreco());
        itemArmazenamento.setOrcamento(orcamento);
        itemArmazenamento.setComponente(armazenamento);

        ItemOrcamentoModel itemMonitor = new ItemOrcamentoModel();
        itemMonitor.setQuantidade(1);
        itemMonitor.setPreco(monitor.getPreco());
        itemMonitor.setOrcamento(orcamento);
        itemMonitor.setComponente(monitor);

        itemOrcamentoRepository.saveAll(Arrays.asList(
                itemCpu,
                itemPlacaMae,
                itemGpu,
                itemRam,
                itemFonte,
                itemArmazenamento,
                itemMonitor
        ));
    }
}
