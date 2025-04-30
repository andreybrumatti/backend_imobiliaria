package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.EnderecoDTO;
import com.siadsistemas.projeto_siad.model.Endereco;
import com.siadsistemas.projeto_siad.service.EnderecoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/enderecos")
@AllArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<Endereco>> getAll() {
        return ResponseEntity.ok(enderecoService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EnderecoDTO dto) {
        try {
            Endereco novo = enderecoService.create(dto);
            return new ResponseEntity<>(novo, HttpStatus.CREATED);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody EnderecoDTO dto) {
        try {
            dto.endereco().setId(id);
            Endereco atualizado = enderecoService.update(dto);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<?> inativarPorId(@PathVariable UUID id) {
        try {
            enderecoService.inativarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
