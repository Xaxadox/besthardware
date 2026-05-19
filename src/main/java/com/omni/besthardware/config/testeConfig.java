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
public class testeConfig implements CommandLineRunner {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private perfilRepository perfilRepository;

    @Autowired
    private cpuRepository cpuRepository;

    @Autowired
    private placaMaeRepository placaMaeRepository;

    @Autowired
    private gpuRepository gpuRepository;

    @Autowired
    private ramRepository ramRepository;

    @Autowired
    private fonteRepository fonteRepository;

    @Autowired
    private armazenamentoRepository armazenamentoRepository;

    @Autowired
    private monitorRepository monitorRepository;

    @Autowired
    private orcamentoRepository orcamentoRepository;

    @Autowired
    private itemOrcamentoRepository itemOrcamentoRepository;

    @Override
    public void run(String... args) throws Exception {

        // Evita duplicar dados se a aplicação reiniciar
        if (usuarioRepository.count() > 0) {
            return;
        }

        // =========================
        // USUÁRIOS
        // =========================
        usuarioModel usuario1 = new usuarioModel();
        usuario1.setNome("Caio Rosa");
        usuario1.setEmail("caio.rosa@email.com");

        usuarioModel usuario2 = new usuarioModel();
        usuario2.setNome("Agnaldo");
        usuario2.setEmail("agnaldo@email.com");

        usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2));

        // =========================
        // PERFIS
        // =========================
        perfilModel perfilGamer = new perfilModel();
        perfilGamer.setNome("Gamer");

        perfilModel perfilTrabalho = new perfilModel();
        perfilTrabalho.setNome("Trabalho");

        perfilRepository.saveAll(Arrays.asList(perfilGamer, perfilTrabalho));

        // =========================
        // COMPONENTES
        // =========================

        cpuModel cpu = new cpuModel();
        cpu.setTipo("CPU");
        cpu.setPreco(new BigDecimal("899.90"));
        cpu.setModelo("Ryzen 5 5600");
        cpu.setFrequencia(3500);
        cpu.setConsumo(65);
        cpu.setAnoLancamento(LocalDate.of(2022, 4, 4));
        cpu.setNucleos(6);
        cpu.setSocket("AM4");

        placaMaeModel placaMae = new placaMaeModel();
        placaMae.setTipo("PlacaMae");
        placaMae.setPreco(new BigDecimal("699.90"));
        placaMae.setMarca("ASUS");
        placaMae.setSocket("AM4");
        placaMae.setChipset("B550");
        placaMae.setFormato("ATX");

        gpuModel gpu = new gpuModel();
        gpu.setTipo("GPU");
        gpu.setPreco(new BigDecimal("1899.90"));
        gpu.setModelo("RTX 3060");
        gpu.setMemoria(12);
        gpu.setConsumo(170);
        gpu.setMarca("NVIDIA");

        ramModel ram = new ramModel();
        ram.setTipo("DDR4");
        ram.setPreco(new BigDecimal("449.90"));
        ram.setFrequencia(3200);
        ram.setMarca("Kingston");
        ram.setMemoria(8);

        fonteModel fonte = new fonteModel();
        fonte.setTipo("Fonte");
        fonte.setPreco(new BigDecimal("399.90"));
        fonte.setMarca("Corsair");
        fonte.setPotencia(650);
        fonte.setCertificacao("80 Plus Bronze");

        armazenamentoModel armazenamento = new armazenamentoModel();
        armazenamento.setTipo("SSD");
        armazenamento.setPreco(new BigDecimal("299.90"));
        armazenamento.setEntrada("NVME");
        armazenamento.setVelocidadeEscrita(3000);
        armazenamento.setVelocidadeLeitura(3500);
        armazenamento.setMemoria(256);

        monitorModel monitor = new monitorModel();
        monitor.setTipo("IPS");
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
        orcamentoModel orcamento = new orcamentoModel();
        orcamento.setNome("PC Gamer AM4");
        orcamento.setDataCriacao(LocalDateTime.now());
        orcamento.setUsuario(usuario1);
        orcamento.setPerfil(perfilGamer);
        orcamento.setPreco(new BigDecimal("5349.40"));

        orcamentoRepository.save(orcamento);

        // =========================
        // ITENS DO ORÇAMENTO
        // =========================
        itemOrcamentoModel itemCpu = new itemOrcamentoModel();
        itemCpu.setQuantidade(1);
        itemCpu.setPreco(cpu.getPreco());
        itemCpu.setOrcamento(orcamento);
        itemCpu.setComponente(cpu);

        itemOrcamentoModel itemPlacaMae = new itemOrcamentoModel();
        itemPlacaMae.setQuantidade(1);
        itemPlacaMae.setPreco(placaMae.getPreco());
        itemPlacaMae.setOrcamento(orcamento);
        itemPlacaMae.setComponente(placaMae);

        itemOrcamentoModel itemGpu = new itemOrcamentoModel();
        itemGpu.setQuantidade(1);
        itemGpu.setPreco(gpu.getPreco());
        itemGpu.setOrcamento(orcamento);
        itemGpu.setComponente(gpu);

        itemOrcamentoModel itemRam = new itemOrcamentoModel();
        itemRam.setQuantidade(2);
        itemRam.setPreco(ram.getPreco());
        itemRam.setOrcamento(orcamento);
        itemRam.setComponente(ram);

        itemOrcamentoModel itemFonte = new itemOrcamentoModel();
        itemFonte.setQuantidade(1);
        itemFonte.setPreco(fonte.getPreco());
        itemFonte.setOrcamento(orcamento);
        itemFonte.setComponente(fonte);

        itemOrcamentoModel itemArmazenamento = new itemOrcamentoModel();
        itemArmazenamento.setQuantidade(1);
        itemArmazenamento.setPreco(armazenamento.getPreco());
        itemArmazenamento.setOrcamento(orcamento);
        itemArmazenamento.setComponente(armazenamento);

        itemOrcamentoModel itemMonitor = new itemOrcamentoModel();
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