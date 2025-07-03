package com.github.questionarium.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.github.questionarium.model.User;
import com.github.questionarium.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        userRepository.deleteAll();

        // Usuários Admin
        userRepository.save(new User(1L, "Admin 1", null, null));
        userRepository.save(new User(2L, "Admin 2", null, null));
        // Usuários comuns
        userRepository.save(new User(3L, "User 1", null, null));
        userRepository.save(new User(4L, "User 2", null, null));
        userRepository.save(new User(5L, "João", null, null));

    }

}
