package br.com.questionarium.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.questionarium.auth.model.AuthUser;
import br.com.questionarium.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoDBUserDetailsService implements UserDetailsService {

    private final AuthUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Carregando usuário pelo login: {}", username);
        try {
            AuthUser user = userRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
            log.info("Usuário encontrado: {}", username);
            return user;
        } catch (UsernameNotFoundException e) {
            log.error("Falha ao carregar usuário: {}", username);
            throw e;
        }
    }
}
