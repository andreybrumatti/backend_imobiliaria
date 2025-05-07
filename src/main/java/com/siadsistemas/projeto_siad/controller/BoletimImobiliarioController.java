package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.BoletimImobiliarioDTO;
import com.siadsistemas.projeto_siad.model.BoletimImobiliario;
import com.siadsistemas.projeto_siad.service.BoletimImobiliarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/boletimImobiliario")
@AllArgsConstructor
public class BoletimImobiliarioController {

    private final BoletimImobiliarioService boletimService;

    @GetMapping("/listarTodos")
    public ResponseEntity<List<BoletimImobiliario>> getAll() {
        return ResponseEntity.ok(boletimService.findAll());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> create(@RequestBody BoletimImobiliarioDTO dto) {
        try {
            BoletimImobiliario novo = boletimService.create(dto);
            return new ResponseEntity<>(novo, HttpStatus.CREATED);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody BoletimImobiliarioDTO dto) {
        try {
            BoletimImobiliario atualizado = boletimService.update(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativarPorId(@PathVariable UUID id) {
        try {
            boletimService.inativarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
