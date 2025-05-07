package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.ResponsavelLegalDTO;
import com.siadsistemas.projeto_siad.model.ResponsavelLegal;
import com.siadsistemas.projeto_siad.service.ResponsavelLegalService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/responsavelLegal")
@AllArgsConstructor
public class ResponsavelLegalController {

    private final ResponsavelLegalService responsavelLegalService;

    @Operation(summary = "Listar todos os Responsáveis Legais")
    @GetMapping("/listarTodos")
    public ResponseEntity<List<ResponsavelLegal>> getAll() {
        return ResponseEntity.ok(responsavelLegalService.findAll());
    }

    @Operation(summary = "Cadastrar novo Responsável Legal")
    @PostMapping("/cadastrar")
    public ResponseEntity<?> create(@RequestBody ResponsavelLegalDTO dto) {
        try {
            ResponsavelLegal novo = responsavelLegalService.create(dto);
            return new ResponseEntity<>(novo, HttpStatus.CREATED);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualizar os dados do Responsável Legal")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ResponsavelLegalDTO dto) {
        try {
            ResponsavelLegal atualizado = responsavelLegalService.update(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Inativar Responsável Legal")
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
