package br.com.questionarium.user_service.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.user_service.dto.CreateUserRequest;
import br.com.questionarium.user_service.dto.UpdateUserRequest;
import br.com.questionarium.user_service.dto.UserResponse;
import br.com.questionarium.user_service.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        logger.info("POST /users – criando usuário com e-mail {}", request.getEmail());
        UserResponse response = userService.createUser(request);
        logger.info("Usuário criado: ID {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        logger.info("GET /users/confirm – confirmando token {}", token);
        String msg = userService.confirmUser(token);
        logger.info("Confirmação de usuário concluída");
        return ResponseEntity.ok(msg);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        logger.info("GET /users – listando usuários ativos");
        List<UserResponse> users = userService.listAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        logger.info("GET /users/{} – buscando usuário ativo", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        logger.info("GET /users/email – buscando usuário por e-mail {}", email);
        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        logger.info("PUT /users/{} – atualizando usuário", id);
        UserResponse updated = userService.updateUser(id, request);
        logger.info("Usuário {} atualizado", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("DELETE /users/{} – removendo usuário (soft)", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
