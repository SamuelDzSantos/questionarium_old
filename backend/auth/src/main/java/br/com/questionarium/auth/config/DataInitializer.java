package br.com.questionarium.auth.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.questionarium.auth.model.AuthUser;
import br.com.questionarium.auth.repository.AuthUserRepository;
import br.com.questionarium.types.Role;

@Configuration
public class DataInitializer {

        private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

        @Bean("authDataInitializer")
        public CommandLineRunner authDataInitializer(
                        MongoTemplate mongoTemplate,
                        AuthUserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
                return args -> {
                        // Limpa a coleção de usuários de autenticação
                        logger.info("Limpando coleção 'auth_user' do MongoDB...");
                        if (mongoTemplate.collectionExists(AuthUser.class)) {
                                mongoTemplate.dropCollection(AuthUser.class);
                        }
                        logger.info("Coleção limpa.");

                        // Cria usuários iniciais
                        logger.info("Populando credenciais iniciais...");
                        List<AuthUser> initialUsers = List.of(
                                        AuthUser.builder()
                                                        .login("admin1@quest.com")
                                                        .password(passwordEncoder.encode("senha123"))
                                                        .role(Role.ADMIN)
                                                        .build(),
                                        AuthUser.builder()
                                                        .login("admin2@quest.com")
                                                        .password(passwordEncoder.encode("senha123"))
                                                        .role(Role.ADMIN)
                                                        .build(),
                                        AuthUser.builder()
                                                        .login("user1@quest.com")
                                                        .password(passwordEncoder.encode("senha123"))
                                                        .role(Role.USER)
                                                        .build(),
                                        AuthUser.builder()
                                                        .login("user2@quest.com")
                                                        .password(passwordEncoder.encode("senha123"))
                                                        .role(Role.USER)
                                                        .build(),
                                        AuthUser.builder()
                                                        .login("user3@quest.com")
                                                        .password(passwordEncoder.encode("senha123"))
                                                        .role(Role.USER)
                                                        .build());
                        userRepository.saveAll(initialUsers);

                        logger.info("Dados de inicialização realizados com sucesso.");
                };
        }
}
