package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.dto.BairroDTO;
import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.service.BairroService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bairros")
@AllArgsConstructor
public class BairroController {

    private final BairroService bairroService;

    @PostMapping
    public ResponseEntity<Bairro> criarOuBuscar(@RequestBody BairroDTO dto) {
        Bairro bairro = bairroService.buscarOuCriar(dto.nome());
        return ResponseEntity.ok(bairro);
    }
}