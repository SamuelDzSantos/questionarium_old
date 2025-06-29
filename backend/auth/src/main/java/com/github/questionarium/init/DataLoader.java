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

        // Não encontrei uma configuração que retirasse registros automáticamente no
        // MongoDB. Garante que não serão criados registros duplicados por meio da
        // limpeza a cada restart da aplicação.
        userRepository.deleteAll();
        databaseSequenceRepository.deleteAll();

        User user1 = new User(null, "admin1@quest.com", passwordEncoder.encode("senha123"), UserRole.ADMIN, true,
                false);
        User user2 = new User(null, "user2@user3.com", passwordEncoder.encode("1234"), UserRole.USER, true, false);
        User user3 = new User(null, "user1@quest.com", passwordEncoder.encode("senha123"), UserRole.USER, true, false);

        User user4 = new User(null, "user2@quest.com", passwordEncoder.encode("senha123"), UserRole.USER, true, false);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
    }

}
