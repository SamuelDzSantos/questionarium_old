package dev.questionarium.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.questionarium.entities.UserData;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RestTemplateUserDetailsService implements UserDetailsService {

    private final RestTemplate template;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData userData = template.getForObject("http://user-service/users?email={email}", UserData.class, username);
        if (userData == null)
            throw new UsernameNotFoundException("Dados do usuário nulos!");

        List<GrantedAuthority> authorities = new ArrayList<>();
        userData.roles().forEach((role) -> authorities.add(new SimpleGrantedAuthority(role)));
        System.out.println(userData);
        User userDetails = new User(userData.name(), userData.password(), authorities);

        return userDetails;
    }

}
