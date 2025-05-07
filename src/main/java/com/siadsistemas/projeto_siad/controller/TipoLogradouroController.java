package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.model.TipoLogradouro;
import com.siadsistemas.projeto_siad.service.TipoLogradouroService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-logradouro")
@AllArgsConstructor
public class TipoLogradouroController {

    private final TipoLogradouroService tipoLogradouroService;

    @Operation(summary = "Listar todos os tipos de logradouro")
    @GetMapping("/listarTodos")
    public List<TipoLogradouro> listarTodos() {
        return tipoLogradouroService.listarTodos();
    }
}