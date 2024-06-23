package org.ufpr.questionarium.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ufpr.questionarium.dtos.LoginResult;
import org.ufpr.questionarium.dtos.UserData;
import org.ufpr.questionarium.dtos.UserPatch;
import org.ufpr.questionarium.services.AuthenticationService;
import org.ufpr.questionarium.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/current")
    public ResponseEntity<UserData> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok().body(this.userService.getCurrentUser(authentication));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(Authentication authentication, @PathVariable Long id) {
        this.userService.deleteUser(authentication, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoginResult> updateUserFields(Authentication authentication, @RequestBody UserPatch patch,
            @PathVariable Long id) {
        return ResponseEntity.ok().body(this.authenticationService.updateUserAuth(authentication, patch, id));
    }

}
