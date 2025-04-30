package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.ResponsavelLegalDTO;
import com.siadsistemas.projeto_siad.model.ResponsavelLegal;
import com.siadsistemas.projeto_siad.service.ResponsavelLegalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/responsavel-legal")
@AllArgsConstructor
public class ResponsavelLegalController {

    private final ResponsavelLegalService responsavelLegalService;

    @GetMapping
    public ResponseEntity<List<ResponsavelLegal>> getAll() {
        return ResponseEntity.ok(responsavelLegalService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ResponsavelLegalDTO dto) {
        try {
            ResponsavelLegal novo = responsavelLegalService.create(dto);
            return new ResponseEntity<>(novo, HttpStatus.CREATED);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ResponsavelLegalDTO dto) {
        try {
            ResponsavelLegal atualizado = responsavelLegalService.update(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativarPorId(@PathVariable UUID id) {
        try {
            responsavelLegalService.inativarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
