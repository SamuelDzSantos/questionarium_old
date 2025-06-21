package br.com.questionarium.user_service.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.questionarium.user_service.model.User;
import br.com.questionarium.user_service.repository.UserRepository;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean("initialUserData")
    public CommandLineRunner dataInitializer(UserRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                // Admin users
                User admin1 = new User("admin1", "admin1@quest.com", List.of("ADMIN"));
                admin1.setActive(true);
                repo.save(admin1);

                User admin2 = new User("admin2", "admin2@quest.com", List.of("ADMIN"));
                admin2.setActive(true);
                repo.save(admin2);

                // Regular users
                User user1 = new User("user1", "user1@quest.com", List.of("USER"));
                user1.setActive(true);
                repo.save(user1);

                User user2 = new User("user2", "user2@quest.com", List.of("USER"));
                user2.setActive(true);
                repo.save(user2);

                User user3 = new User("user3", "user3@quest.com", List.of("USER"));
                user3.setActive(true);
                repo.save(user3);

                logger.info("Dados de inicialização criados com sucesso.");
            }
        };
    }
}
