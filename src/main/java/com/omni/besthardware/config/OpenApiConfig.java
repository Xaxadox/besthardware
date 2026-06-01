package com.omni.besthardware.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bestHardwareOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Best Hardware API")
                        .version("1.0.0")
                        .description("API para cadastro de componentes, orcamentos, compatibilidade e recomendacoes de PCs."));
    }
}
