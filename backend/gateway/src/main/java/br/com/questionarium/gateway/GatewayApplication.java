package br.com.questionarium.gateway;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(RouteDefinitionLocator locator) {
		return args -> locator.getRouteDefinitions().subscribe(def -> {
			System.out.println("[Gateway] Route Definition: " + def.getId() + " -> " + def.getUri());
			System.out.println("[Gateway] Predicates: " + def.getPredicates());
		});
	}

}
