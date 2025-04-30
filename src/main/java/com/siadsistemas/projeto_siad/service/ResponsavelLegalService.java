package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.ResponsavelLegalDTO;
import com.siadsistemas.projeto_siad.model.Endereco;
import com.siadsistemas.projeto_siad.model.ResponsavelLegal;
import com.siadsistemas.projeto_siad.repository.EnderecoRepository;
import com.siadsistemas.projeto_siad.repository.ResponsavelLegalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResponsavelLegalService {

    private final ResponsavelLegalRepository responsavelLegalRepository;
    private final EnderecoRepository enderecoRepository;

    public List<ResponsavelLegal> findAll() {
        return responsavelLegalRepository.findAllByAtivoTrue();
    }

    @Transactional
    public ResponsavelLegal create(ResponsavelLegalDTO dto) {
        validarCampos(dto);

        if (responsavelLegalRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (responsavelLegalRepository.existsByNumeroDocumentoAndTipoPessoa(dto.numeroDocumento(), dto.tipoPessoa())) {
            throw new IllegalArgumentException("Documento já cadastrado para esse tipo de pessoa.");
        }

        ResponsavelLegal responsavel = new ResponsavelLegal();
        responsavel.setCodigo(responsavelLegalRepository.findMaxCodigo() + 1);
        responsavel.setTipoPessoa(dto.tipoPessoa());
        responsavel.setNome(dto.nome());
        responsavel.setTelefoneFixo(dto.telefoneFixo());
        responsavel.setTelefoneCelular(dto.telefoneCelular());
        responsavel.setEmail(dto.email());
        responsavel.setNumeroDocumento(dto.numeroDocumento());

        Endereco endereco = enderecoRepository.findByIdAndAtivoTrue(dto.endereco().getId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id: " + dto.endereco().getId()));

        responsavel.setEndereco(endereco);
        responsavel.setAtivo(true);
        responsavel.setCreated_at(LocalDateTime.now());
        responsavel.setUpdated_at(LocalDateTime.now());

        return responsavelLegalRepository.save(responsavel);
    }

    @Transactional
    public ResponsavelLegal update(UUID id, ResponsavelLegalDTO dto) {
        ResponsavelLegal existente = responsavelLegalRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Responsável legal não encontrado com id: " + id));

        validarCampos(dto);

        if (!existente.getEmail().equalsIgnoreCase(dto.email()) &&
                responsavelLegalRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (!existente.getNumeroDocumento().equalsIgnoreCase(dto.numeroDocumento()) ||
                existente.getTipoPessoa() != dto.tipoPessoa()) {
            if (responsavelLegalRepository.existsByNumeroDocumentoAndTipoPessoa(dto.numeroDocumento(), dto.tipoPessoa())) {
                throw new IllegalArgumentException("Documento já cadastrado para esse tipo de pessoa.");
            }
        }

        existente.setTipoPessoa(dto.tipoPessoa());
        existente.setNome(dto.nome());
        existente.setTelefoneFixo(dto.telefoneFixo());
        existente.setTelefoneCelular(dto.telefoneCelular());
        existente.setEmail(dto.email());
        existente.setNumeroDocumento(dto.numeroDocumento());

        Endereco endereco = enderecoRepository.findByIdAndAtivoTrue(dto.endereco().getId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id: " + dto.endereco().getId()));
        existente.setEndereco(endereco);

        existente.setUpdated_at(LocalDateTime.now());

        return responsavelLegalRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        ResponsavelLegal existente = responsavelLegalRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Responsável legal não encontrado com id: " + id));

        existente.setAtivo(false);
        existente.setUpdated_at(LocalDateTime.now());
        responsavelLegalRepository.save(existente);
    }

    private void validarCampos(ResponsavelLegalDTO dto) {
        if (dto.tipoPessoa() == null) {
            throw new IllegalArgumentException("Tipo de pessoa é obrigatório.");
        }
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (dto.numeroDocumento() == null || dto.numeroDocumento().isBlank()) {
            throw new IllegalArgumentException("Número do documento é obrigatório.");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        if (dto.endereco().getId() == null) {
            throw new IllegalArgumentException("Endereço é obrigatório.");
        }
    }
}