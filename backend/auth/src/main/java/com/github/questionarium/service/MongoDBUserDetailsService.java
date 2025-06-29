package com.github.questionarium.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.questionarium.model.AuthUser;
import com.github.questionarium.model.User;
import com.github.questionarium.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoDBUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Carregando usuário pelo login: {}", username);
        try {
            User user = userRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
            log.info("Usuário encontrado: {}", username);
            if (!user.getActive()) {
                throw new UsernameNotFoundException("Usuário desativado: " + username);
            }
            return new AuthUser(user);
        } catch (UsernameNotFoundException e) {
            log.error("Falha ao carregar usuário: {} com erro: \n {}", username, e.getMessage());
            throw e;
        }
    }
}
