package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.LogradouroDTO;
import com.siadsistemas.projeto_siad.exception.domain.logradouro.LogradouroException;
import com.siadsistemas.projeto_siad.exception.domain.logradouro.LogradouroNotFoundException;
import com.siadsistemas.projeto_siad.model.Logradouro;
import com.siadsistemas.projeto_siad.model.TipoLogradouro;
import com.siadsistemas.projeto_siad.repository.LogradouroRepository;
import com.siadsistemas.projeto_siad.repository.TipoLogradouroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LogradouroService {

    private final LogradouroRepository logradouroRepository;
    private final TipoLogradouroRepository tipoLogradouroRepository;

    public List<Logradouro> findAll() {
        return logradouroRepository.findAllByAtivoTrueOrderByCodigoAsc();
    }

    @Transactional
    public Logradouro create(LogradouroDTO dto) {

        if (dto.nome() == null || dto.nome().isEmpty() || dto.tipo_logradouro() == null) {
            throw new LogradouroException("Dados inválidos: Nome e Tipo do logradouro são obrigatórios.");
        }

        Optional<Logradouro> existente = logradouroRepository.findByNomeAndAtivoTrueIgnoreCase(dto.nome());

        if(existente.isPresent()) {
            throw new LogradouroException("Logradouro já cadastrado com o nome: " + dto.nome());
        }

        Logradouro logradouro = new Logradouro();
        logradouro.setNome(dto.nome());
        logradouro.setNome_anterior(dto.nome());

        logradouro.setTipo_logradouro(buscarTipoLogradouro(dto.tipo_logradouro()));


        logradouro.setAtivo(true);
        logradouro.setCreated_at(LocalDateTime.now());
        logradouro.setUpdated_at(LocalDateTime.now());

        Integer novoCodigo = logradouroRepository.findMaxCodigo() + 1;
        logradouro.setCodigo(novoCodigo);

        return logradouroRepository.save(logradouro);
    }

    @Transactional
    public Logradouro buscarOuCriar(String nome, Integer tipoLogradouro) {

        Optional<Logradouro> log  = logradouroRepository.findByNomeAndAtivoTrueIgnoreCase(nome);

        if(log.isPresent()){
            Logradouro logEditado = log.get();

            TipoLogradouro newTipo = tipoLogradouroRepository.findByCodigo(tipoLogradouro)
                    .orElseThrow(() -> new LogradouroNotFoundException(
                            "Tipo de logradouro não encontrado com código: " + tipoLogradouro));

            logEditado.setTipo_logradouro(newTipo);

            return logradouroRepository.save(logEditado);
        }

        Logradouro novoLogradouro = new Logradouro();
        novoLogradouro.setNome(nome);
        novoLogradouro.setNome_anterior(nome);

        novoLogradouro.setTipo_logradouro(buscarTipoLogradouro(tipoLogradouro));

        novoLogradouro.setAtivo(true);
        novoLogradouro.setCreated_at(LocalDateTime.now());
        novoLogradouro.setUpdated_at(LocalDateTime.now());
        Integer novoCodigo = logradouroRepository.findMaxCodigo() + 1;
        novoLogradouro.setCodigo(novoCodigo);

        return logradouroRepository.save(novoLogradouro);
    }

    private Logradouro findById(UUID id) {
        return logradouroRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new LogradouroNotFoundException("Logradouro ativo não encontrado com id: " + id));
    }

    @Transactional
    public Logradouro update(UUID id, LogradouroDTO dto) {
        Logradouro existente = findById(id);

        if (!existente.getNome().equalsIgnoreCase(dto.nome())) {
            existente.setNome_anterior(existente.getNome());
            existente.setNome(dto.nome());
        }

        existente.setTipo_logradouro(buscarTipoLogradouro(dto.tipo_logradouro()));
        existente.setUpdated_at(LocalDateTime.now());

        return logradouroRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        Logradouro existente = logradouroRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new LogradouroNotFoundException("Logradouro ativo não encontrado com código: " + id));

        existente.setAtivo(false);
        existente.setUpdated_at(LocalDateTime.now());

        logradouroRepository.save(existente);
    }

    private TipoLogradouro buscarTipoLogradouro(Integer codigo) {
        return tipoLogradouroRepository.findByCodigo(codigo)
                .orElseThrow(() -> new LogradouroNotFoundException(
                        "Tipo de logradouro não encontrado com código: " + codigo));
    }
}
