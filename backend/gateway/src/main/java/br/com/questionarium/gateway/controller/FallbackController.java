package br.com.questionarium.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/auth")
    public ResponseEntity<String> authFallback() {
        return ResponseEntity.ok("Serviço de AUTENTICAÇÃO está temporariamente indiponível.");
    }
    
    @RequestMapping("/users")
    public ResponseEntity<String> usersFallback() {
        return ResponseEntity.ok("Serviço de USUARIO está temporariamente indiponível.");
    }
    
    @RequestMapping("/questions")
    public ResponseEntity<String> questionsFallback() {
        return ResponseEntity.ok("Serviço de QUESTÕES está temporariamente indiponível.");
    }
}