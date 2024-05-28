package org.ufpr.questionarium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ufpr.questionarium.config.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)

@SpringBootApplication
@RestController
public class QuestionariumApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionariumApplication.class, args);
	}

	@GetMapping
	public String apiMain() {
		return "";
	}

}
