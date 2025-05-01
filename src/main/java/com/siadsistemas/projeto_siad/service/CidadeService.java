package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.model.Cidade;
import com.siadsistemas.projeto_siad.repository.CidadeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    public Cidade buscarOuCriar(String nome) {
        return cidadeRepository
                .findByNomeIgnoreCase(nome.trim())
                .orElseGet(() -> {
                    Cidade nova = new Cidade();
                    nova.setNome(nome.trim());
                    nova.setAtivo(true);
                    nova.setCreated_at(LocalDateTime.now());
                    nova.setUpdated_at(LocalDateTime.now());

                    Integer novoCodigo = cidadeRepository.findMaxCodigo() + 1;
                    nova.setCodigo(novoCodigo);

                    return cidadeRepository.save(nova);
                });
    }
}