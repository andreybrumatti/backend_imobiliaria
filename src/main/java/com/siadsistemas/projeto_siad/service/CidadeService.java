package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.model.Cidade;
import com.siadsistemas.projeto_siad.repository.CidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

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

                    Integer novoCodigo = cidadeRepository.findMaxCodigo() + 1;
                    nova.setCodigo(novoCodigo);

                    return cidadeRepository.save(nova);
                });
    }

    public Cidade findByCodigo(Integer id) {
        return cidadeRepository.findByCodigo(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com id: " + id));
    }
}