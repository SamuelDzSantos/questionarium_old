package org.ufpr.questionarium;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.FilterChainProxy;





@SpringBootApplication
public class QuestionariumApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionariumApplication.class, args);
			
	}

	@Bean
	CommandLineRunner commandLineRunner(FilterChainProxy filterChainProxy){
		return args -> {
			filterChainProxy.getFilterChains()
				.stream()
				.forEach(filterChain -> {
					System.out.println("Chain1");
					filterChain.getFilters().stream().forEach(filter->{System.out.println(filter);});
				});
		};
	}
}
