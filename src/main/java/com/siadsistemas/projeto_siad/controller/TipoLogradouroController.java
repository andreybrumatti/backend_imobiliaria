package com.siadsistemas.projeto_siad.controller;

import com.siadsistemas.projeto_siad.model.TipoLogradouro;
import com.siadsistemas.projeto_siad.service.TipoLogradouroService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tipo-logradouro")
@AllArgsConstructor
public class TipoLogradouroController {

    private final TipoLogradouroService tipoLogradouroService;

    @GetMapping
    public List<TipoLogradouro> listarTodos() {
        return tipoLogradouroService.listarTodos();
    }
}