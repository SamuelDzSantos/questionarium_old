package org.ufpr.questionarium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

// Classe de configuração global de alguns dos comportamentos do Spring MVC

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    // Define uma url base para todos os RestControllers da aplicação não sendo
    // necessário mencionar o /api nos caminhos dos
    // RestControllers(GetMapping e RequestMapping)

    @Override
    public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
    }

    // Define que os arquivos static estão armazenados em /static/browser e define
    // /resources/ como o caminho(url) para acessar esses recursos

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/static/browser/");
    }

    // Configura o thymeleaf para procurar por arquivos html no caminho
    // /static/browser. É utilizado para servir o html base do angular

    @Bean
    ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/static/browser/");
        templateResolver.setCacheable(false);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    // Configuração de Cors Global

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

}
