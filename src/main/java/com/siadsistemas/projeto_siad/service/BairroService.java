package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.repository.BairroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BairroService {

    private final BairroRepository bairroRepository;

    public Bairro buscarOuCriar(String nome) {
        return bairroRepository
                .findByNomeIgnoreCase(nome.trim())
                .orElseGet(() -> {
                    Bairro novo = new Bairro();
                    novo.setNome(nome.trim());
                    novo.setAtivo(true);
                    novo.setCreated_at(LocalDateTime.now());
                    novo.setUpdated_at(LocalDateTime.now());

                    Integer novoCodigo = bairroRepository.findMaxCodigo() + 1;
                    novo.setCodigo(novoCodigo);

                    return bairroRepository.save(novo);
                });
    }
}