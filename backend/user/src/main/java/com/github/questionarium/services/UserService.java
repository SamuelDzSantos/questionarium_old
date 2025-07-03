package com.github.questionarium.services;

import org.springframework.stereotype.Service;

import com.github.questionarium.model.User;
import com.github.questionarium.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User register(Long id, String name) {

        User user = userRepository.findById(id).orElse(null);
        // Caso usuário já exista apenas continua a SAGA, caso contrario cria o usuário.
        if (user == null) {
            return userRepository.save(new User(id, name, null, null, null));
        }
        return user;
    }

    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User saveImage(Long userId, byte[] data) {

        User user = userRepository.findById(userId).orElse(null);
        System.out.println(data);
        log.info("Atualizando imagem para user: {}", user);
        if (user != null) {
            user.setImage(data);
            user = userRepository.save(user);
            log.info("Novo user: {}", user);
            return user;
        }
        return user;
    }

}
