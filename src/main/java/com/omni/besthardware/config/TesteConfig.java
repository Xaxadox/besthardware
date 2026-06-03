package com.omni.besthardware.config;

import com.omni.besthardware.model.*;
import com.omni.besthardware.repository.*;

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

    @Autowired
    private OfertaPrecoRepository ofertaPrecoRepository;

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

        CpuModel cpuTrabalho = new CpuModel();
        cpuTrabalho.setTipo("CPU");
        cpuTrabalho.setPreco(new BigDecimal("799.90"));
        cpuTrabalho.setModelo("Ryzen 5 5600G");
        cpuTrabalho.setFrequencia(3900);
        cpuTrabalho.setConsumo(65);
        cpuTrabalho.setAnoLancamento(LocalDate.of(2021, 4, 13));
        cpuTrabalho.setNucleos(6);
        cpuTrabalho.setSocket("AM4");

        CpuModel cpuIntermediaria = new CpuModel();
        cpuIntermediaria.setTipo("CPU");
        cpuIntermediaria.setPreco(new BigDecimal("1399.90"));
        cpuIntermediaria.setModelo("Ryzen 5 7600");
        cpuIntermediaria.setFrequencia(3800);
        cpuIntermediaria.setConsumo(65);
        cpuIntermediaria.setAnoLancamento(LocalDate.of(2023, 1, 10));
        cpuIntermediaria.setNucleos(6);
        cpuIntermediaria.setSocket("AM5");

        CpuModel cpuPesada = new CpuModel();
        cpuPesada.setTipo("CPU");
        cpuPesada.setPreco(new BigDecimal("2499.90"));
        cpuPesada.setModelo("Ryzen 7 7800X3D");
        cpuPesada.setFrequencia(4200);
        cpuPesada.setConsumo(120);
        cpuPesada.setAnoLancamento(LocalDate.of(2023, 4, 6));
        cpuPesada.setNucleos(8);
        cpuPesada.setSocket("AM5");

        CpuModel cpuProfissional = new CpuModel();
        cpuProfissional.setTipo("CPU");
        cpuProfissional.setPreco(new BigDecimal("3299.90"));
        cpuProfissional.setModelo("Ryzen 9 7900");
        cpuProfissional.setFrequencia(3700);
        cpuProfissional.setConsumo(65);
        cpuProfissional.setAnoLancamento(LocalDate.of(2023, 1, 10));
        cpuProfissional.setNucleos(12);
        cpuProfissional.setSocket("AM5");

        PlacaMaeModel placaA520 = new PlacaMaeModel();
        placaA520.setTipo("PlacaMae");
        placaA520.setPreco(new BigDecimal("449.90"));
        placaA520.setMarca("Gigabyte");
        placaA520.setSocket("AM4");
        placaA520.setChipset("A520");
        placaA520.setFormato("mATX");

        PlacaMaeModel placaB650 = new PlacaMaeModel();
        placaB650.setTipo("PlacaMae");
        placaB650.setPreco(new BigDecimal("1199.90"));
        placaB650.setMarca("MSI");
        placaB650.setSocket("AM5");
        placaB650.setChipset("B650");
        placaB650.setFormato("ATX");

        PlacaMaeModel placaX670 = new PlacaMaeModel();
        placaX670.setTipo("PlacaMae");
        placaX670.setPreco(new BigDecimal("2199.90"));
        placaX670.setMarca("ASUS");
        placaX670.setSocket("AM5");
        placaX670.setChipset("X670");
        placaX670.setFormato("ATX");

        GpuModel gpuEntrada = new GpuModel();
        gpuEntrada.setTipo("GPU");
        gpuEntrada.setPreco(new BigDecimal("899.90"));
        gpuEntrada.setModelo("GTX 1650");
        gpuEntrada.setMemoria(4);
        gpuEntrada.setConsumo(75);
        gpuEntrada.setMarca("NVIDIA");

        GpuModel gpuIntermediaria = new GpuModel();
        gpuIntermediaria.setTipo("GPU");
        gpuIntermediaria.setPreco(new BigDecimal("2499.90"));
        gpuIntermediaria.setModelo("RTX 4060 Ti");
        gpuIntermediaria.setMemoria(8);
        gpuIntermediaria.setConsumo(160);
        gpuIntermediaria.setMarca("NVIDIA");

        GpuModel gpuPesada = new GpuModel();
        gpuPesada.setTipo("GPU");
        gpuPesada.setPreco(new BigDecimal("5499.90"));
        gpuPesada.setModelo("RTX 4070 Ti Super");
        gpuPesada.setMemoria(16);
        gpuPesada.setConsumo(285);
        gpuPesada.setMarca("NVIDIA");

        GpuModel gpuProfissional = new GpuModel();
        gpuProfissional.setTipo("GPU");
        gpuProfissional.setPreco(new BigDecimal("7999.90"));
        gpuProfissional.setModelo("RTX 4080 Super");
        gpuProfissional.setMemoria(16);
        gpuProfissional.setConsumo(320);
        gpuProfissional.setMarca("NVIDIA");

        RamModel ramDdr4_16 = new RamModel();
        ramDdr4_16.setTipo("RAM");
        ramDdr4_16.setGeracao("DDR4");
        ramDdr4_16.setPreco(new BigDecimal("329.90"));
        ramDdr4_16.setFrequencia(3200);
        ramDdr4_16.setMarca("Kingston");
        ramDdr4_16.setMemoria(16);

        RamModel ramDdr5_16 = new RamModel();
        ramDdr5_16.setTipo("RAM");
        ramDdr5_16.setGeracao("DDR5");
        ramDdr5_16.setPreco(new BigDecimal("499.90"));
        ramDdr5_16.setFrequencia(5600);
        ramDdr5_16.setMarca("Corsair");
        ramDdr5_16.setMemoria(16);

        RamModel ramDdr5_32 = new RamModel();
        ramDdr5_32.setTipo("RAM");
        ramDdr5_32.setGeracao("DDR5");
        ramDdr5_32.setPreco(new BigDecimal("899.90"));
        ramDdr5_32.setFrequencia(6000);
        ramDdr5_32.setMarca("Corsair");
        ramDdr5_32.setMemoria(32);

        FonteModel fonte450 = new FonteModel();
        fonte450.setTipo("Fonte");
        fonte450.setPreco(new BigDecimal("249.90"));
        fonte450.setMarca("Corsair");
        fonte450.setPotencia(450);
        fonte450.setCertificacao("80 Plus Bronze");

        FonteModel fonte550 = new FonteModel();
        fonte550.setTipo("Fonte");
        fonte550.setPreco(new BigDecimal("329.90"));
        fonte550.setMarca("Cooler Master");
        fonte550.setPotencia(550);
        fonte550.setCertificacao("80 Plus Bronze");

        FonteModel fonte750 = new FonteModel();
        fonte750.setTipo("Fonte");
        fonte750.setPreco(new BigDecimal("699.90"));
        fonte750.setMarca("XPG");
        fonte750.setPotencia(750);
        fonte750.setCertificacao("80 Plus Gold");

        FonteModel fonte850 = new FonteModel();
        fonte850.setTipo("Fonte");
        fonte850.setPreco(new BigDecimal("899.90"));
        fonte850.setMarca("Corsair");
        fonte850.setPotencia(850);
        fonte850.setCertificacao("80 Plus Gold");

        ArmazenamentoModel ssd512 = new ArmazenamentoModel();
        ssd512.setTipo("Armazenamento");
        ssd512.setTecnologia("SSD");
        ssd512.setPreco(new BigDecimal("349.90"));
        ssd512.setPadrao("NVME");
        ssd512.setVelocidadeEscrita(2500);
        ssd512.setVelocidadeLeitura(3500);
        ssd512.setMemoria(512);

        ArmazenamentoModel ssd1Tb = new ArmazenamentoModel();
        ssd1Tb.setTipo("Armazenamento");
        ssd1Tb.setTecnologia("SSD");
        ssd1Tb.setPreco(new BigDecimal("549.90"));
        ssd1Tb.setPadrao("NVME");
        ssd1Tb.setVelocidadeEscrita(5000);
        ssd1Tb.setVelocidadeLeitura(7000);
        ssd1Tb.setMemoria(1000);

        ArmazenamentoModel ssd2Tb = new ArmazenamentoModel();
        ssd2Tb.setTipo("Armazenamento");
        ssd2Tb.setTecnologia("SSD");
        ssd2Tb.setPreco(new BigDecimal("999.90"));
        ssd2Tb.setPadrao("NVME");
        ssd2Tb.setVelocidadeEscrita(6000);
        ssd2Tb.setVelocidadeLeitura(7300);
        ssd2Tb.setMemoria(2000);

        MonitorModel monitorFhd75 = new MonitorModel();
        monitorFhd75.setTipo("Monitor");
        monitorFhd75.setTecnologia("IPS");
        monitorFhd75.setPreco(new BigDecimal("599.90"));
        monitorFhd75.setMarca("LG");
        monitorFhd75.setTamanho("24");
        monitorFhd75.setResolucao("1920x1080");
        monitorFhd75.setFrequencia(75);

        MonitorModel monitor2k144 = new MonitorModel();
        monitor2k144.setTipo("Monitor");
        monitor2k144.setTecnologia("IPS");
        monitor2k144.setPreco(new BigDecimal("1699.90"));
        monitor2k144.setMarca("AOC");
        monitor2k144.setTamanho("27");
        monitor2k144.setResolucao("2560x1440");
        monitor2k144.setFrequencia(144);

        MonitorModel monitor4k144 = new MonitorModel();
        monitor4k144.setTipo("Monitor");
        monitor4k144.setTecnologia("IPS");
        monitor4k144.setPreco(new BigDecimal("3999.90"));
        monitor4k144.setMarca("Gigabyte");
        monitor4k144.setTamanho("28");
        monitor4k144.setResolucao("3840x2160");
        monitor4k144.setFrequencia(144);

        MonitorModel monitor4k60 = new MonitorModel();
        monitor4k60.setTipo("Monitor");
        monitor4k60.setTecnologia("IPS");
        monitor4k60.setPreco(new BigDecimal("2199.90"));
        monitor4k60.setMarca("Dell");
        monitor4k60.setTamanho("27");
        monitor4k60.setResolucao("3840x2160");
        monitor4k60.setFrequencia(60);

        cpuRepository.saveAll(Arrays.asList(cpuTrabalho, cpuIntermediaria, cpuPesada, cpuProfissional));
        placaMaeRepository.saveAll(Arrays.asList(placaA520, placaB650, placaX670));
        gpuRepository.saveAll(Arrays.asList(gpuEntrada, gpuIntermediaria, gpuPesada, gpuProfissional));
        ramRepository.saveAll(Arrays.asList(ramDdr4_16, ramDdr5_16, ramDdr5_32));
        fonteRepository.saveAll(Arrays.asList(fonte450, fonte550, fonte750, fonte850));
        armazenamentoRepository.saveAll(Arrays.asList(ssd512, ssd1Tb, ssd2Tb));
        monitorRepository.saveAll(Arrays.asList(monitorFhd75, monitor2k144, monitor4k144, monitor4k60));

        LocalDate dataColeta = LocalDate.now();
        ofertaPrecoRepository.saveAll(Arrays.asList(
                oferta(cpu, "KaBuM", "849.90", "899.90", 10, null, "https://exemplo.com/ryzen-5-5600-kabum", "Pesquisa manual", dataColeta, "Preco a vista sem frete"),
                oferta(cpu, "Pichau", "829.90", "879.90", 10, null, "https://exemplo.com/ryzen-5-5600-pichau", "PC Build Wizard", dataColeta, "Menor oferta inicial para teste"),
                oferta(gpu, "Terabyte", "1799.90", "1899.90", 10, null, "https://exemplo.com/rtx-3060-terabyte", "Pesquisa manual", dataColeta, "GPU Full HD"),
                oferta(cpuTrabalho, "KaBuM", "749.90", "799.90", 10, null, "https://exemplo.com/ryzen-5-5600g-kabum", "PC Build Wizard", dataColeta, "CPU com video integrado"),
                oferta(gpuIntermediaria, "Pichau", "2399.90", "2499.90", 10, null, "https://exemplo.com/rtx-4060-ti-pichau", "PC Build Wizard", dataColeta, "Oferta para perfil 2K"),
                oferta(gpuPesada, "Terabyte", "5299.90", "5499.90", 10, null, "https://exemplo.com/rtx-4070-ti-super-terabyte", "Pesquisa manual", dataColeta, "Oferta para jogos pesados"),
                oferta(ramDdr5_32, "KaBuM", "849.90", "899.90", 10, null, "https://exemplo.com/ddr5-32gb-kabum", "Pesquisa manual", dataColeta, "Memoria para jogos pesados e profissional"),
                oferta(fonte850, "Pichau", "849.90", "899.90", 10, null, "https://exemplo.com/fonte-850w-pichau", "Pesquisa manual", dataColeta, "Fonte para configuracao profissional"),
                oferta(ssd1Tb, "KaBuM", "499.90", "549.90", 10, null, "https://exemplo.com/ssd-1tb-kabum", "PC Build Wizard", dataColeta, "SSD NVME para jogos atuais"),
                oferta(monitor2k144, "Terabyte", "1599.90", "1699.90", 10, null, "https://exemplo.com/monitor-2k-144-terabyte", "Pesquisa manual", dataColeta, "Monitor 2K")
        ));

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

    private OfertaPrecoModel oferta(
            ComponenteModel componente,
            String loja,
            String precoAvista,
            String precoParcelado,
            Integer parcelas,
            String cupom,
            String urlProduto,
            String fonte,
            LocalDate dataColeta,
            String observacoes
    ) {
        OfertaPrecoModel oferta = new OfertaPrecoModel();
        oferta.setComponente(componente);
        oferta.setLoja(loja);
        oferta.setPrecoAvista(new BigDecimal(precoAvista));
        oferta.setPrecoParcelado(new BigDecimal(precoParcelado));
        oferta.setParcelas(parcelas);
        oferta.setCupom(cupom);
        oferta.setUrlProduto(urlProduto);
        oferta.setFonte(fonte);
        oferta.setDataColeta(dataColeta);
        oferta.setObservacoes(observacoes);
        return oferta;
    }
}
