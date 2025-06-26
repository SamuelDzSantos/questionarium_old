package br.com.questionarium.gateway;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class GatewayApplication {

	private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(RouteDefinitionLocator locator) {
		return args -> locator.getRouteDefinitions().subscribe(def -> {
			String now = LocalDateTime.now().format(TS);
			log.info("[{}] [Gateway] Route Definition: {} -> {}", now, def.getId(), def.getUri());
			log.info("[{}] [Gateway]   Predicates: {}", now, def.getPredicates());
		});
	}
}
