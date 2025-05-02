package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.repository.BairroRepository;
import com.siadsistemas.projeto_siad.repository.CidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BairroService {

    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;

    public Bairro buscarOuCriar(String nome, Integer cidade_codigo) {

        Optional <Bairro> existente = bairroRepository.findByNomeAndAtivoTrueIgnoreCase(nome);

        if(existente.isPresent()) {
            Bairro existenteEditado = existente.get();

            existenteEditado.setNome(nome);
            existenteEditado.setCidade(cidadeRepository.findByCodigo(cidade_codigo)
                    .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com código: " + cidade_codigo)));
            existenteEditado.setAtivo(true);
            existenteEditado.setUpdated_at(LocalDateTime.now());
            return bairroRepository.save(existenteEditado);
        }

        Bairro bairro = new Bairro();
        bairro.setNome(nome);
        bairro.setCidade(cidadeRepository.findByCodigo(cidade_codigo)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada com código: " + cidade_codigo)));
        bairro.setAtivo(true);
        bairro.setCreated_at(LocalDateTime.now());
        bairro.setUpdated_at(LocalDateTime.now());
        Integer novoCodigo = bairroRepository.findMaxCodigo() + 1;
        bairro.setCodigo(novoCodigo);
        return bairroRepository.save(bairro);
    }
}