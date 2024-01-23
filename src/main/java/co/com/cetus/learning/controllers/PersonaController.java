package co.com.cetus.learning.controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.cetus.learning.microservicio_cliente_contactos.service.WebFluxService;
import co.com.cetus.learning.model.Persona;
import co.com.cetus.learning.model.ResultAuth;
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
    private WebFluxService webFluxService;

    String uriJwt = "/login";
    String user = "admin";
    String password = "admin";
    String token;

    @Value("${external-api.keycloack}")
    String urlAuthenticator;

    private static final String USERNAME = "admin";
    private static final String PASSWORD_LOGIN = "admin";
    private static final String CLIENT_ID = "login";
    private static final String GRANT_TYPE = "password";
    private HttpHeaders headers = new HttpHeaders();

    @PostConstruct()
    public void autenticar() {
        WebClient authWebClient = WebClient.builder()
                .baseUrl(urlAuthenticator)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        MultiValueMap<String, String> authData = new LinkedMultiValueMap<>();

        authData.add("client_id", CLIENT_ID);
        authData.add("username", USERNAME);
        authData.add("password", PASSWORD_LOGIN);
        authData.add("grant_type", GRANT_TYPE);
        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<ResultAuth> response = authWebClient
                .post()
                .uri("")
                .body(BodyInserters.fromFormData(authData))
                .retrieve()
                .bodyToMono(ResultAuth.class)
                .map(t -> {
                    // JsonNode rootNode=mapper.readTree(t);
                    // ResultAuth ra= new ResultAuth(rootNode.get());
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
                    HttpStatus status = HttpStatus.OK;
                    return new ResponseEntity<ResultAuth>(t, responseHeaders, status);
                })
                .block();
        log.info("RESPUESTA JSONWEBTOKEN: " + response.getBody());
        headers.add("Authorization", "Bearer " + response
                .getBody().getAccessToken());
        // response.getBody().getAccessToken());

    }

    @GetMapping(value = "personas/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Persona> altaPersona(@PathVariable("nombre") String nombre, @PathVariable("email") String email,
            @PathVariable("edad") int edad) {
        Persona persona = new Persona(nombre, email, edad);
        Mono<Persona> posting = webFluxService.postContacto(persona, this.headers);
        posting.subscribe(t -> log.info(t.getNombre(), "Se ha registrado"));
        posting.block();
        Mono<Persona[]> lista = webFluxService.getContactos(this.headers);
        Persona[] listaResultado = lista.block();
        return Arrays.asList(listaResultado);
    }

    @GetMapping("")
    public String getMethodName() {
        return "Funciona";
    }

}