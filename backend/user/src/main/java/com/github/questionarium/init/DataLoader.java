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

        User user1 = new User(1L, "User1", null, null);
        User user2 = new User(2L, "User2", null, null);
        User user3 = new User(3L, "User3", null, null);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

    }

}
