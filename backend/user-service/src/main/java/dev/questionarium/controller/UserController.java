package dev.questionarium.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.entities.UserData;
import dev.questionarium.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserData> save(@RequestBody RegistrationRequest userRegistration) {
        return ResponseEntity.ok(this.userService.save(userRegistration));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserData> getUser(@PathVariable String id) {
        return ResponseEntity.ok(this.userService.getUserById(Long.parseLong(id)));
    }
}
