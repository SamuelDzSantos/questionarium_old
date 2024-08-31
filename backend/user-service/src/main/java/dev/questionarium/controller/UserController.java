package dev.questionarium.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.questionarium.entities.AuthRequest;
import dev.questionarium.entities.AuthResponse;
import dev.questionarium.entities.PasswordPatch;
import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.entities.ResetPasswordValidation;
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
    public ResponseEntity<UserData> save(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(this.userService.save(registrationRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserData> getUser(@PathVariable String id) {
        return ResponseEntity.ok(this.userService.getUserById(Long.parseLong(id)));
    }

    @GetMapping("/current")
    public ResponseEntity<UserData> getCurrentUser(@RequestHeader("x-user-id") String id) {
        return ResponseEntity.ok(this.userService.getUserById(Long.parseLong(id)));
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(this.userService.login(authRequest));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordPatch patch) {
        this.forgotPasswordService.resetPassword(patch);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> checkIfCodeIsValid(@RequestBody ResetPasswordValidation validation) {
        return ResponseEntity.ok().body(this.forgotPasswordService.checkCodeValue(validation));
    }

    @GetMapping("/password-reset")
    public ResponseEntity<Void> generatePasswordToken(@RequestParam String email) {
        this.forgotPasswordService.forgotPassword(email);
        return ResponseEntity.status(HttpStatus.SC_CREATED).build();
    }
}
