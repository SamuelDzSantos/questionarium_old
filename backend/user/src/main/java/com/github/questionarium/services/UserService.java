package com.github.questionarium.services;

import org.springframework.stereotype.Service;

import com.github.questionarium.model.User;
import com.github.questionarium.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(Long id, String name) {

        User user = userRepository.findById(id).orElse(null);
        // Caso usuário já exista apenas continua a SAGA, caso contrario cria o usuário.
        if (user == null) {
            return userRepository.save(new User(id, name, null, null));
        }
        return user;
    }

    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

}
