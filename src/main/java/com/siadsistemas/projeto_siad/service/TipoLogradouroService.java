package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.model.TipoLogradouro;
import com.siadsistemas.projeto_siad.repository.TipoLogradouroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TipoLogradouroService {

    private final TipoLogradouroRepository tipoLogradouroRepository;

    public List<TipoLogradouro> listarTodos() {
        return tipoLogradouroRepository.findAllByAtivoTrue();
    }
}