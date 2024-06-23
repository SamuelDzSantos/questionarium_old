package org.ufpr.questionarium.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ufpr.questionarium.exceptions.exceptions.NoSuchElementFoundException;
import org.ufpr.questionarium.models.SecurityUser;
import org.ufpr.questionarium.repositories.UserRepository;

// Implementação do UserDetailsService utilizada para buscar o usuário no banco de dados e retornar um objeto UserDetails.
// É utilizado em conjunto com o DaoAuthenticationProvider e AuthenticationManager no arquivo de configuração SecurityConfig.java

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser userDetails = this.userRepository.findByEmail(username).map(SecurityUser::new)
                .orElseThrow(
                        () -> new NoSuchElementFoundException(String.format("Usuário %s não encontrado!", username)));
        return userDetails;
    }

}