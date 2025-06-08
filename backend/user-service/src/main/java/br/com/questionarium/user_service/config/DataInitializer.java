package br.com.questionarium.user_service.config;

import br.com.questionarium.user_service.dto.CreateUserRequest;
import br.com.questionarium.user_service.dto.UserResponse;
import br.com.questionarium.user_service.repository.UserRepository;
import br.com.questionarium.user_service.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, UserService userService) {
        return args -> {
            if (userRepository.count() == 0) {
                System.out.println("Criando usuários de teste...");

                // Cria
                UserResponse admin1 = userService.createUser(
                        new CreateUserRequest("Admin 1", "admin1@quest.com", "senha123", List.of("ADMIN")));
                UserResponse admin2 = userService.createUser(
                        new CreateUserRequest("Admin 2", "admin2@quest.com", "senha123", List.of("ADMIN")));
                UserResponse user1 = userService
                        .createUser(new CreateUserRequest("User 1", "user1@quest.com", "senha123", List.of("USER")));
                UserResponse user2 = userService
                        .createUser(new CreateUserRequest("User 2", "user2@quest.com", "senha123", List.of("USER")));

                // Ativa
                userService.activateUser(admin1.getId());
                userService.activateUser(admin2.getId());
                userService.activateUser(user1.getId());
                userService.activateUser(user2.getId());

                System.out.println("Usuários de teste criados e ativados.");
            } else {
                System.out.println("Usuários já existem. Nenhum dado de teste foi criado.");
            }
        };
    }
}
