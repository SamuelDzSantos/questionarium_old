package br.net.dac.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.net.dac.auth.model.AuthUser;
import br.net.dac.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MongoDBUserDetailsService implements UserDetailsService {

    private final AuthUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AuthUser user = this.userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + " não encontrado!"));
        return user;
    }

}
