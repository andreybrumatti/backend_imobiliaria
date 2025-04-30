package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.EnderecoDTO;
import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.model.Endereco;
import com.siadsistemas.projeto_siad.model.Logradouro;
import com.siadsistemas.projeto_siad.repository.BairroRepository;
import com.siadsistemas.projeto_siad.repository.EnderecoRepository;
import com.siadsistemas.projeto_siad.repository.LogradouroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final LogradouroRepository logradouroRepository;
    private final BairroRepository bairroRepository;

    public List<Endereco> findAll() {
        return enderecoRepository.findAllByAtivoTrue();
    }

    @Transactional
    public Endereco create(EnderecoDTO dto) {
        validarCamposParaCriacao(dto);

        Endereco endereco = new Endereco();
        endereco.setCodigo(enderecoRepository.findMaxCodigo() + 1);
        endereco.setLogradouro(getLogradouro(dto.logradouro().getId()));
        endereco.setBairro(getBairro(dto.endereco().getBairro().getId()));
        endereco.setNumero(dto.endereco().getNumero());
        endereco.setComplemento(dto.endereco().getComplemento());
        endereco.setCep(dto.endereco().getCep());
        endereco.setAtivo(true);
        endereco.setCreated_at(LocalDateTime.now());
        endereco.setUpdated_at(LocalDateTime.now());

        return enderecoRepository.save(endereco);
    }

    @Transactional
    public Endereco update(EnderecoDTO dto) {
        Endereco existente = enderecoRepository.findByIdAndAtivoTrue(dto.endereco().getId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + dto.endereco().getId()));

        validarCamposParaAtualizacao(dto);

        existente.setLogradouro(getLogradouro(dto.logradouro().getId()));
        existente.setBairro(getBairro(dto.endereco().getBairro().getId()));
        existente.setNumero(dto.endereco().getNumero());
        existente.setComplemento(dto.endereco().getComplemento());
        existente.setCep(dto.endereco().getCep());
        existente.setUpdated_at(LocalDateTime.now());

        return enderecoRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        Endereco existente = enderecoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));

        existente.setAtivo(false);
        existente.setUpdated_at(LocalDateTime.now());
        enderecoRepository.save(existente);
    }

    private void validarCamposParaCriacao(EnderecoDTO dto) {
        if (dto.logradouro() == null || dto.logradouro().getId()== null) {
            throw new IllegalArgumentException("Código do logradouro é obrigatório.");
        }
        if (dto.endereco() == null || dto.endereco().getBairro() == null || dto.endereco().getBairro().getId() == null) {
            throw new IllegalArgumentException("Código do bairro é obrigatório.");
        }
        if (dto.endereco().getNumero() == null || dto.endereco().getCep() == null) {
            throw new IllegalArgumentException("Número e CEP são obrigatórios.");
        }
    }

    private void validarCamposParaAtualizacao(EnderecoDTO dto) {
        if (dto.endereco() == null || dto.endereco().getId() == null) {
            throw new IllegalArgumentException("Código do endereço é obrigatório para atualização.");
        }
        validarCamposParaCriacao(dto); // Reaproveita as mesmas validações do create
    }

    private Logradouro getLogradouro(UUID id) {
        return logradouroRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Logradouro não encontrado com código: " + id));
    }

    private Bairro getBairro(UUID id) {
        return bairroRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Bairro não encontrado com código: " + id));
    }
}
