package org.ufpr.questionarium.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.ufpr.questionarium.services.UserService;

@Component
public class LoadDatabase implements CommandLineRunner {

    private final UserService userService;

    public LoadDatabase(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userService.addUser("user1", "myemail@gmail.com", "1234");
        this.userService.addUser("example", "example@email.com", "senha123");
    };

}