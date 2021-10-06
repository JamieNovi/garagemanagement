package nl.jamienovi.garagemanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition
public class GaragemanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaragemanagementApplication.class, args);
	}

}
