package co.com.cetus.learning.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import co.com.cetus.learning.microservicio_cliente_contactos.service.WebFluxService;
import co.com.cetus.learning.model.Persona;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * PersonaController
 */
@CrossOrigin
@RestController
@Slf4j
public class PersonaController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private WebFluxService webFluxService;

    String uriJwt = "/login";
    String user = "admin";
    String password = "admin";
    String token;

    @PostConstruct()
    public void autenticar() {
        token = webClient.post()
                .uri(uriJwt + "?user={user}&password={password}", user, password)
                .retrieve().bodyToMono(String.class).block();

    }

    @GetMapping(value = "personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Persona> altaPersona(@PathVariable("nombre") String nombre, @PathVariable("email") String email,
            @PathVariable("edad") int edad) {
        Persona persona = new Persona(nombre, email, edad);
        Mono<Persona> posting = webFluxService.postContacto(persona, this.token);
        posting.subscribe(t -> log.info(t.getNombre(), "Se ha registrado"));
        posting.block();
        Mono<Persona[]> lista = webFluxService.getContactos(this.token);
        Persona[] listaResultado = lista.block();
        return Arrays.asList(listaResultado);
    }

    @GetMapping("")
    public String getMethodName() {
        return "Funciona";
    }

}