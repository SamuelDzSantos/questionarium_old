package dev.questionarium.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.questionarium.entities.PasswordPatch;
import dev.questionarium.entities.UserData;
import dev.questionarium.service.ForgotPasswordService;
import dev.questionarium.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private final UserService userService;
    private final ForgotPasswordService forgotPasswordService;

    @GetMapping("/current")
    public ResponseEntity<UserData> getCurrentUser(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordPatch patch) {
        this.forgotPasswordService.resetPassword(patch.token(), patch.password(), patch.code());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/password-reset/{token}")
    public ResponseEntity<Void> checkIfPasswordTokenIsValid(@PathVariable String token) {
        this.forgotPasswordService.checkPasswordToken(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> getPasswordToken(@RequestParam String email) {
        return ResponseEntity.ok(this.forgotPasswordService.forgotPassword(email));
    }

}
