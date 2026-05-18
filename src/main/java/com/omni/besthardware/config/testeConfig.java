package com.omni.besthardware.config;

import com.omni.besthardware.models.usuarioModel;
import com.omni.besthardware.repositories.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class testeConfig implements CommandLineRunner {

    @Autowired
    private usuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Exemplo de carga inicial (Instanciar objetos e guardar na base de dados)
        usuarioModel u1 = new usuarioModel();
        u1.setNome("Caio Rosa");
        u1.setEmail("caio.rosa@email.com");

        usuarioModel u2 = new usuarioModel();
        u2.setNome("Agnaldo");
        u2.setEmail("agnaldo@email.com");

        usuarioRepository.saveAll(Arrays.asList(u1, u2));
    }
}