package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.LogradouroDTO;
import com.siadsistemas.projeto_siad.model.Logradouro;
import com.siadsistemas.projeto_siad.service.LogradouroService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/logradouro")
@AllArgsConstructor
public class LogradouroController {

    private final LogradouroService logradouroService;

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Logradouro>> getAll() {
        return ResponseEntity.ok(logradouroService.findAll());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> create(@RequestBody LogradouroDTO dto) {
        try {
            Logradouro novo = logradouroService.create(dto);
            return new ResponseEntity<>(novo, HttpStatus.CREATED);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody LogradouroDTO dto) {
        try {
            Logradouro atualizado = logradouroService.update(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativarPorId(@PathVariable UUID id) {
        try {
            logradouroService.inativarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}