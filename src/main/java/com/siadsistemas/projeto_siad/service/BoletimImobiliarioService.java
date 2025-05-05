package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.BoletimImobiliarioDTO;
import com.siadsistemas.projeto_siad.dto.ResponsavelLegalDTO;
import com.siadsistemas.projeto_siad.model.*;
import com.siadsistemas.projeto_siad.repository.BoletimImobiliarioRepository;
import com.siadsistemas.projeto_siad.repository.EnderecoRepository;
import com.siadsistemas.projeto_siad.repository.ResponsavelLegalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BoletimImobiliarioService {

    private final BoletimImobiliarioRepository boletimRepository;
    private  final ResponsavelLegalRepository responsavelLegalRepository;
    private final ResponsavelLegalService  responsavelLegalService;

    public List<BoletimImobiliario> findAll() {
        return boletimRepository.findAllByAtivoTrue();
    }

    @Transactional
    public BoletimImobiliario create(BoletimImobiliarioDTO dto) {
        if (boletimRepository.existsByMatricula(dto.matricula())) {
            throw new IllegalArgumentException("Já existe um boletim com esta matrícula.");
        }

        validarCampos(dto);
        validarUnicidade(dto.responsavelLegal());

        Endereco endereco = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.endereco());
        Endereco enderecoFisico = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoFisico());
        Endereco enderecoCorrespondencia = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoCorrespondencia());

        BoletimImobiliario bci = new BoletimImobiliario();
        bci.setCodigo(boletimRepository.findMaxCodigo() + 1);
        bci.setMatricula(dto.matricula());

        bci.setEndereco(endereco);
        bci.setEnderecoFisico(enderecoFisico);
        bci.setEnderecoCorrespondencia(enderecoCorrespondencia);

        bci.setAtivo(true);
        bci.setCreated_at(LocalDateTime.now());
        bci.setUpdated_at(LocalDateTime.now());

        return boletimRepository.save(bci);
    }

    @Transactional
    public BoletimImobiliario update(UUID id, BoletimImobiliarioDTO dto) {
        BoletimImobiliario existente = boletimRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Boletim não encontrado com id: " + id));

        if (!existente.getMatricula().equalsIgnoreCase(dto.matricula()) && //se a matrícula foi alterada
                boletimRepository.existsByMatricula(dto.matricula())) { //e for igual a alguma matrícula existente
            throw new IllegalArgumentException("Já existe um boletim com esta matrícula.");
        }

        validarCampos(dto);

        Endereco enderecoFisico = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoFisico());
        Endereco enderecoCorrespondencia = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoCorrespondencia());

        existente.setMatricula(dto.matricula());

        ResponsavelLegal responsavel = criarResponsavelLegal(dto.responsavelLegal());
        existente.setResponsavelLegal(responsavel);

        existente.setEnderecoFisico(enderecoFisico);
        existente.setEnderecoCorrespondencia(enderecoCorrespondencia);
        existente.setUpdated_at(LocalDateTime.now());

        return boletimRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        BoletimImobiliario existente = boletimRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Boletim não encontrado com id: " + id));

        existente.setAtivo(false);
        existente.setUpdated_at(LocalDateTime.now());

        boletimRepository.save(existente);
    }

    private void validarCampos(BoletimImobiliarioDTO dto) {
        if (dto.matricula() == null || dto.matricula().isBlank()) {
            throw new IllegalArgumentException("Matrícula é obrigatória.");
        }
        if (dto.responsavelLegal() == null) {
            throw new IllegalArgumentException("Responsável legal é obrigatório.");
        }
        if (dto.enderecoFisico() == null) {
            throw new IllegalArgumentException("Endereço físico é obrigatório.");
        }
        if (dto.enderecoCorrespondencia() == null) {
            throw new IllegalArgumentException("Endereço de correspondência é obrigatório.");
        }
    }

    public void validarUnicidade(ResponsavelLegalDTO dto) {

        if(responsavelLegalRepository.existsByNomeIgnoreCaseAndAtivoTrue(dto.nome())){
            throw new IllegalArgumentException("Nome de Responsável já cadastrado.");
        }

        if (responsavelLegalRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (responsavelLegalRepository.existsByNumeroDocumentoAndTipoPessoa(dto.numeroDocumento(), dto.tipoPessoa())) {
            throw new IllegalArgumentException("Documento já cadastrado para esse tipo de pessoa.");
        }
    }


    public ResponsavelLegal criarResponsavelLegal(ResponsavelLegalDTO dto) {

        validarUnicidade(dto);

        ResponsavelLegal responsavel = new ResponsavelLegal();
        responsavel.setTipoPessoa(dto.tipoPessoa());
        responsavel.setNome(dto.nome());
        responsavel.setTelefoneFixo(dto.telefoneFixo());
        responsavel.setTelefoneCelular(dto.telefoneCelular());
        responsavel.setEmail(dto.email());
        responsavel.setNumeroDocumento(dto.numeroDocumento());

        Endereco endereco = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.endereco());
        responsavel.setEndereco(endereco);

        responsavel.setAtivo(true);
        responsavel.setCodigo(responsavelLegalRepository.findMaxCodigo() + 1);
        responsavel.setCreated_at(LocalDateTime.now());
        responsavel.setUpdated_at(LocalDateTime.now());

        return responsavelLegalRepository.save(responsavel);
    }

}
