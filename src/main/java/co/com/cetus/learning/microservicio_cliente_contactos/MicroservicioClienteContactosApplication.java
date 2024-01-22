package co.com.cetus.learning.microservicio_cliente_contactos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@ComponentScan(basePackages = { "co.com.cetus.learning.controllers",
		"co.com.cetus.learning",
})
public class MicroservicioClienteContactosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioClienteContactosApplication.class, args);
	}

}
