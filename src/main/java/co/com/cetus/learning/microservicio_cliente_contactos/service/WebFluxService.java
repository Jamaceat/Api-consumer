package co.com.cetus.learning.microservicio_cliente_contactos.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import co.com.cetus.learning.model.Persona;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WebFluxService {

    final WebClient webClient;

    WebFluxService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Persona> postContacto(Persona persona, String token) {
        log.info("llegue a Mono<Persona>");
        return webClient.post()
                .uri("/contactos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(persona))
                .retrieve()
                .bodyToMono(Persona.class);

    }

    public Mono<Persona[]> getContactos(String token) {
        log.info("llegue a Mono<Persona[]> lista");
        return webClient.get()
                .uri("/contactos")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Persona[].class);

    }

}
