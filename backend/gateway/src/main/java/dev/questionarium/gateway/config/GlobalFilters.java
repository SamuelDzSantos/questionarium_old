package dev.questionarium.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

@Configuration
public class GlobalFilters {

    @Bean
    GlobalFilter AuthenticationGlobalFilter() {
        return (exchange, chain) -> {
            System.out.println(exchange.getRequest().getPath());
            return Mono.empty();
        };
    }
}
