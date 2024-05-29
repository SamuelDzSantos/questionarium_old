package org.ufpr.questionarium.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ufpr.questionarium.dtos.LogResult;
import org.ufpr.questionarium.dtos.LoggedUser;
import org.ufpr.questionarium.dtos.LoginForm;
import org.ufpr.questionarium.dtos.RegisterForm;
import org.ufpr.questionarium.dtos.UpdatedUserForm;
import org.ufpr.questionarium.services.JwtUtils;
import org.ufpr.questionarium.services.UserService;

@RestController
@RequestMapping("/")
@Configuration
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LogResult> login(@RequestBody LoginForm login) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(),
                login.getPassword());
        System.out.println("Trying to connect!");
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        System.out.println("Generating token for user " + authenticationResponse.getName());
        String token = jwtUtils.generateToken(authenticationResponse);
        System.out.println("Token granted " + token);
        LoggedUser user = this.userService.getUser(authenticationResponse);
        LogResult result = new LogResult(user.getUsername(), user.getEmail(), token);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterForm registerForm) {
        this.userService.register(registerForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<LoggedUser> getUser(Authentication authentication) {
        return ResponseEntity.ok().body(this.userService.getUser(authentication));
    }

    @PostMapping("/update/user")
    public ResponseEntity<Void> updateUser(Authentication authentication, @RequestBody UpdatedUserForm userForm) {
        this.userService.updateUser(authentication, userForm);
        return ResponseEntity.ok().build();
    }

}