package com.github.questionarium.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.questionarium.model.User;
import com.github.questionarium.repository.DatabaseSequenceRepository;
import com.github.questionarium.repository.UserRepository;
import com.github.questionarium.types.UserRole;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final DatabaseSequenceRepository databaseSequenceRepository;

        @Override
        public void run(ApplicationArguments args) throws Exception {

                userRepository.deleteAll();
                databaseSequenceRepository.deleteAll();

                // Usuários Admin
                userRepository
                                .save(new User(null, "admin1@quest.com", passwordEncoder.encode("1234"), UserRole.ADMIN,
                                                true, false));
                userRepository
                                .save(new User(null, "admin2@quest.com", passwordEncoder.encode("1234"), UserRole.ADMIN,
                                                true, false));
                // Usuários comuns
                userRepository
                                .save(new User(null, "user1@quest.com", passwordEncoder.encode("1234"), UserRole.USER,
                                                true, false));
                userRepository
                                .save(new User(null, "user2@quest.com", passwordEncoder.encode("1234"), UserRole.USER,
                                                true, false));
                userRepository
                                .save(new User(null, "joao@quest.com", passwordEncoder.encode("1234"), UserRole.USER,
                                                true, false));

                userRepository.save(
                                new User(null, "samueldzsantos@gmail.com", passwordEncoder.encode("1234"),
                                                UserRole.USER, true,
                                                false));
        }

}
