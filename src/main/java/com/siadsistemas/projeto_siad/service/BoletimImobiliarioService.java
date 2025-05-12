package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.BoletimImobiliarioDTO;
import com.siadsistemas.projeto_siad.dto.ResponsavelLegalDTO;
import com.siadsistemas.projeto_siad.exception.domain.boletimImobiliario.BoletimImobiliarioException;
import com.siadsistemas.projeto_siad.exception.domain.boletimImobiliario.BoletimImobiliarioNotFoundException;
import com.siadsistemas.projeto_siad.model.*;
import com.siadsistemas.projeto_siad.repository.BoletimImobiliarioRepository;
import com.siadsistemas.projeto_siad.repository.ResponsavelLegalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BoletimImobiliarioService {

    private final BoletimImobiliarioRepository boletimRepository;
    private  final ResponsavelLegalRepository responsavelLegalRepository;
    private final ResponsavelLegalService  responsavelLegalService;

    public List<BoletimImobiliario> findAll() {
        return boletimRepository.findAllByAtivoTrueOrderByCodigoAsc();
    }

    @Transactional
    public BoletimImobiliario create(BoletimImobiliarioDTO dto) {
        if (boletimRepository.existsByMatricula(dto.matricula())) {
            throw new BoletimImobiliarioException("Já existe um boletim com esta matrícula.");
        }

        validarCampos(dto);
        validarUnicidade(dto.responsavelLegal(), null);

        Endereco enderecoFisico = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoFisico());
        Endereco enderecoCorrespondencia = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoCorrespondencia());

        BoletimImobiliario bci = new BoletimImobiliario();

        ResponsavelLegal responsavel = criarResponsavelLegal(dto.responsavelLegal());
        bci.setResponsavelLegal(responsavel);

        bci.setCodigo(boletimRepository.findMaxCodigo() + 1);
        bci.setMatricula(dto.matricula());

        bci.setEnderecoFisico(enderecoFisico);
        bci.setEnderecoCorrespondencia(enderecoCorrespondencia);

        return boletimRepository.save(bci);
    }

    @Transactional
    public BoletimImobiliario update(UUID id, BoletimImobiliarioDTO dto) {
        BoletimImobiliario existente = boletimRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BoletimImobiliarioNotFoundException("Boletim não encontrado com id: " + id));

        validarCampos(dto);
        validarUnicidade(dto.responsavelLegal(), existente.getResponsavelLegal().getCodigo());

        Endereco enderecoFisico = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoFisico());
        Endereco enderecoCorrespondencia = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.enderecoCorrespondencia());

        existente.setMatricula(dto.matricula());

        // Atualiza os dados do responsável já existente
        ResponsavelLegal responsavel = existente.getResponsavelLegal();
        responsavel.setTipoPessoa(dto.responsavelLegal().tipoPessoa());
        responsavel.setNome(dto.responsavelLegal().nome());
        responsavel.setTelefoneFixo(dto.responsavelLegal().telefoneFixo());
        responsavel.setTelefoneCelular(dto.responsavelLegal().telefoneCelular());
        responsavel.setEmail(dto.responsavelLegal().email());
        responsavel.setNumeroDocumento(dto.responsavelLegal().numeroDocumento());

        Endereco enderecoResponsavel = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.responsavelLegal().endereco());
        responsavel.setEndereco(enderecoResponsavel);

        existente.setEnderecoFisico(enderecoFisico);
        existente.setEnderecoCorrespondencia(enderecoCorrespondencia);

        return boletimRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        BoletimImobiliario existente = boletimRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new BoletimImobiliarioNotFoundException("Boletim não encontrado com id: " + id));

        existente.setAtivo(false);

        boletimRepository.save(existente);
    }

    private void validarCampos(BoletimImobiliarioDTO dto) {
        if (dto.matricula() == null || dto.matricula().isBlank()) {
            throw new BoletimImobiliarioException("Matrícula é obrigatória.");
        }
        if (dto.responsavelLegal() == null) {
            throw new BoletimImobiliarioException("Responsável legal é obrigatório.");
        }
        if (dto.enderecoFisico() == null) {
            throw new BoletimImobiliarioException("Endereço físico é obrigatório.");
        }
        if (dto.enderecoCorrespondencia() == null) {
            throw new BoletimImobiliarioException("Endereço de correspondência é obrigatório.");
        }
    }

    public void validarUnicidade(ResponsavelLegalDTO dto, Integer codigoIgnorar) {
        if (codigoIgnorar == null) {
            if (responsavelLegalRepository.existsByEmail(dto.email())) {
                throw new BoletimImobiliarioException("E-mail já cadastrado.");
            }

            if (responsavelLegalRepository.existsByNumeroDocumentoAndTipoPessoa(dto.numeroDocumento(), dto.tipoPessoa())) {
                throw new BoletimImobiliarioException("Documento já cadastrado para esse tipo de pessoa.");
            }

            if (responsavelLegalRepository.existsByNomeIgnoreCaseAndAtivoTrue(dto.nome())) {
                throw new BoletimImobiliarioException("Nome de Responsável já cadastrado.");
            }
        } else {
            if (responsavelLegalRepository.existsByEmailAndCodigoNot(dto.email(), codigoIgnorar)) {
                throw new BoletimImobiliarioException("E-mail já cadastrado.");
            }

            if (responsavelLegalRepository.existsByNumeroDocumentoAndCodigoNot(dto.numeroDocumento(), codigoIgnorar)) {
                throw new BoletimImobiliarioException("Documento já cadastrado para esse tipo de pessoa.");
            }

            if (responsavelLegalRepository.existsByNomeIgnoreCaseAndAtivoTrueAndCodigoNot(dto.nome(), codigoIgnorar)) {
                throw new BoletimImobiliarioException("Nome de Responsável já cadastrado.");
            }
        }
    }


    public ResponsavelLegal criarResponsavelLegal(ResponsavelLegalDTO dto) {

        ResponsavelLegal responsavel = new ResponsavelLegal();
        responsavel.setTipoPessoa(dto.tipoPessoa());
        responsavel.setNome(dto.nome());
        responsavel.setTelefoneFixo(dto.telefoneFixo());
        responsavel.setTelefoneCelular(dto.telefoneCelular());
        responsavel.setEmail(dto.email());
        responsavel.setNumeroDocumento(dto.numeroDocumento());

        Endereco endereco = responsavelLegalService.buscarOuCriarEnderecoCompleto(dto.endereco());
        responsavel.setEndereco(endereco);

        responsavel.setCodigo(responsavelLegalRepository.findMaxCodigo() + 1);

        return responsavelLegalRepository.save(responsavel);
    }
}
