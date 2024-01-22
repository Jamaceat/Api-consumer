package co.com.cetus.learning.microservicio_cliente_contactos.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ConfigurationWebFlux {

    // Clase de configuración

    @Value("${external-api.cosumes}")
    String consumesThisApi;

    @Bean
    public WebClient webClient() {
        log.info("Definiendo WebClient");
        return WebClient.builder()
                .baseUrl(consumesThisApi)
                .build();
    }

}
