package com.questionarium.assessment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.questionarium.assessment_service.client")
public class AssessmentServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AssessmentServiceApplication.class, args);
	}
}