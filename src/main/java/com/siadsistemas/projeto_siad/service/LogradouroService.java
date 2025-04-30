package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.LogradouroDTO;
import com.siadsistemas.projeto_siad.model.Logradouro;
import com.siadsistemas.projeto_siad.repository.LogradouroRepository;
import com.siadsistemas.projeto_siad.repository.TipoLogradouroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LogradouroService {

    private final LogradouroRepository logradouroRepository;
    private final TipoLogradouroRepository tipoLogradouroRepository;

    public List<Logradouro> findAll() {
        return logradouroRepository.findAllByAtivoTrue();
    }

    @Transactional
    public Logradouro create(LogradouroDTO dto) {

        if (dto.nome() == null || dto.nome().isEmpty() || dto.tipo_logradouro() == null) {
            throw new IllegalArgumentException("Dados inválidos: Nome e Tipo do logradouro são obrigatórios.");
        }

        Logradouro logradouro = new Logradouro();
        logradouro.setNome(dto.nome());
        logradouro.setNome_anterior(dto.nome());

        logradouro.setTipo_logradouro(
                tipoLogradouroRepository.findByCodigo(dto.tipo_logradouro())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Tipo de logradouro não encontrado com código: " + dto.tipo_logradouro()))
        );

        logradouro.setAtivo(true);
        logradouro.setCreated_at(LocalDateTime.now());
        logradouro.setUpdated_at(LocalDateTime.now());

        Integer novoCodigo = logradouroRepository.findMaxCodigo() + 1;
        logradouro.setCodigo(novoCodigo);

        return logradouroRepository.save(logradouro);
    }

    private Logradouro findById(UUID id) {
        return logradouroRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Logradouro ativo não encontrado com id: " + id));
    }

    @Transactional
    public Logradouro update(UUID id, LogradouroDTO dto) {
        Logradouro existente = findById(id);

        if (!existente.getNome().equalsIgnoreCase(dto.nome())) {
            existente.setNome_anterior(existente.getNome());
            existente.setNome(dto.nome());
        }

        existente.setTipo_logradouro(
                tipoLogradouroRepository.findByCodigo(dto.tipo_logradouro())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Tipo de logradouro não encontrado com código: " + dto.tipo_logradouro()))
        );

        existente.setUpdated_at(LocalDateTime.now());

        return logradouroRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        Logradouro existente = logradouroRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Logradouro ativo não encontrado com código: " + id));

        existente.setAtivo(false);
        existente.setUpdated_at(LocalDateTime.now());

        logradouroRepository.save(existente);
    }
}
