package org.ufpr.questionarium.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ufpr.questionarium.models.SecurityUser;
import org.ufpr.questionarium.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = this.userRepository.findByEmail(username).map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário de email " + username + "não encontrado!"));
        return user;
    }

}