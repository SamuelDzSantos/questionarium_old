package dev.questionarium.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.questionarium.entities.PasswordPatch;
import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.entities.UserData;
import dev.questionarium.service.ForgotPasswordService;
import dev.questionarium.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping
    public ResponseEntity<UserData> save(@RequestBody RegistrationRequest userRegistration) {
        return ResponseEntity.ok(this.userService.save(userRegistration));
    }

    @GetMapping
    public ResponseEntity<UserData> getUserByEmail(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok(this.userService.getUserByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserData> getUser(@PathVariable String id) {
        return ResponseEntity.ok(this.userService.getUserById(Long.parseLong(id)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPass(@RequestParam(name = "email") String email) {
        return ResponseEntity.ok(this.forgotPasswordService.forgotPassword(email));
    }

    @PutMapping("/update-password")
    public ResponseEntity<Boolean> updatePass(@RequestBody PasswordPatch patch) {
        return ResponseEntity.ok(this.forgotPasswordService.resetPassword(patch.token(), patch.password()));
    }

}
