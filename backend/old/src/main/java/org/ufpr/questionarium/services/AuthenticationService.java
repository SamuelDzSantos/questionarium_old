package org.ufpr.questionarium.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.ufpr.questionarium.dtos.LoginForm;
import org.ufpr.questionarium.dtos.LoginResult;
import org.ufpr.questionarium.dtos.RegisterForm;
import org.ufpr.questionarium.dtos.UserData;
import org.ufpr.questionarium.dtos.UserPatch;
import org.ufpr.questionarium.models.SecurityUser;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    public LoginResult login(LoginForm login) {

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(),
                login.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        String token = jwtUtils.generateToken(authenticationResponse);

        UserData loggedUser = this.userService.getUserByEmail(authenticationRequest, login.getEmail());

        LoginResult result = new LoginResult(loggedUser, token);

        return result;
    }

    public UserData register(RegisterForm registerForm) {
        return this.userService.addUser(registerForm);
    }

    public LoginResult updateUserAuth(Authentication authentication, UserPatch patch, Long id) {

        SecurityUser user = this.userService.updateUserFields(authentication, patch, id);

        String token = this.jwtUtils.generateTokenFromSecurityUser(user);

        UserData loggedUser = new UserData(id, user.getUser().getName(), user.getUser().getEmail());

        LoginResult result = new LoginResult(loggedUser, token);

        return result;

    }

}
