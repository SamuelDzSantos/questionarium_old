package br.com.questionarium.question_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.questionarium.question_service.dto.HeaderDTO;
import br.com.questionarium.question_service.service.HeaderService;

@RestController
@RequestMapping("/questions/headers")
public class QuestionHeaderController {
    
    private final HeaderService headerService;

    public QuestionHeaderController(HeaderService headerService) {
        this.headerService = headerService;
    }

    @PostMapping
    public ResponseEntity<HeaderDTO> createHeader(@RequestBody HeaderDTO headerDTO) {
        HeaderDTO createdHeader = headerService.createHeader(headerDTO);
        return new ResponseEntity<>(createdHeader, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HeaderDTO>> getAllHeader() {
        List<HeaderDTO> headers = headerService.getAllHeaders();
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeaderDTO> getHeaderById(@PathVariable Long id) {
        try {
            HeaderDTO headerDTO = headerService.getHeaderById(id);
            return new ResponseEntity<>(headerDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeaderDTO> updateHeader(@PathVariable Long id, @RequestBody HeaderDTO headerDTO) {
        try {
            HeaderDTO updatedHeader = headerService.updateHeader(id, headerDTO);
            return new ResponseEntity<>(updatedHeader, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        try {
            headerService.deleteHeader(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
