package dev.questionarium.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.service.UserService;
import dev.questionarium.types.Role;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class dataLoader implements ApplicationRunner {

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        userService.save(RegistrationRequest.builder()
                .email("myemail@gmail.com")
                .password("1234")
                .name("user1")
                .role(Role.USER)
                .build());

        userService.save(RegistrationRequest.builder()
                .email("myemail2@gmail.com")
                .password("12345")
                .name("user1")
                .role(Role.USER)
                .build());

    }

}
