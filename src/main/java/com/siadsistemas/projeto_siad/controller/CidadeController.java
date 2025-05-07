package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.CidadeDTO;
import com.siadsistemas.projeto_siad.model.Cidade;
import com.siadsistemas.projeto_siad.service.CidadeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cidade")
@AllArgsConstructor
public class CidadeController {

    private final CidadeService cidadeService;

    @Operation(summary = "Buscar ou criar uma cidade")
    @PostMapping
    public ResponseEntity<Cidade> buscarOuCriar(@RequestBody CidadeDTO dto) {
        Cidade cidade = cidadeService.buscarOuCriar(dto.nome());
        return ResponseEntity.ok(cidade);
    }
}