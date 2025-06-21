package br.com.questionarium.user_service.controller;

import java.util.List;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import br.com.questionarium.user_service.dto.ConfirmUserRequest;
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

    /**
     * Criação de usuário: público, sem autenticação.
     */
    @PostMapping
    @PermitAll
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        logger.info("POST /users – criando usuário com e-mail {}", request.getEmail());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Confirmação de e-mail: público, sem autenticação.
     */
    @PostMapping("/confirm")
    @PermitAll
    public ResponseEntity<String> confirmUser(
            @Valid @RequestBody ConfirmUserRequest request) {
        logger.info("POST /users/confirm – confirmando token {}", request.getToken());
        String msg = userService.confirmUser(request.getToken());
        return ResponseEntity.ok(msg);
    }

    /**
     * Listar todos (ativos e inativos): só ADMIN.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        logger.info("GET /users/all – listando TODOS os usuários");
        return ResponseEntity.ok(userService.listAllIncludingInactive());
    }

    /**
     * Listar só ativos: só ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listActiveUsers() {
        logger.info("GET /users – listando usuários ATIVOS");
        List<UserResponse> users = userService.listAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Buscar por ID: só ADMIN.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        logger.info("GET /users/{} – buscando usuário ativo", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Buscar por e-mail: só ADMIN (ajuste se for regra diferente).
     */
    @GetMapping("/email")
    @PermitAll
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        logger.info("GET /users/email – buscando usuário por e-mail {}", email);
        UserResponse user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Atualizar nome/roles: ADMIN ou o próprio usuário.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest req) {

        // 1) coleta informações de quem chamou
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Long principalId = Long.valueOf(auth.getName()); // usamos claim "userId" como principal

        // 2) ninguém não-admin pode mexer em outro ID
        if (!isAdmin && !principalId.equals(id)) {
            throw new AccessDeniedException("Permissão negada: não é possível atualizar outro usuário.");
        }

        // 3) monta a requisição efetiva
        UpdateUserRequest effective = new UpdateUserRequest();
        effective.setName(req.getName());

        // só quem é admin pode mexer em roles
        if (isAdmin) {
            effective.setRoles(req.getRoles());
        }

        // 4) delega ao service
        UserResponse updated = userService.updateUser(id, effective);
        return ResponseEntity.ok(updated);
    }

    /**
     * Soft-delete: ADMIN ou o próprio usuário.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // 1) coleta informações de quem chamou
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Long principalId = Long.valueOf(auth.getName()); // claim "userId" como principal

        // 2) ninguém não-admin pode mexer em outro ID
        if (!isAdmin && !principalId.equals(id)) {
            throw new AccessDeniedException("Permissão negada: não é possível remover outro usuário.");
        }

        // 3) delega ao service
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
