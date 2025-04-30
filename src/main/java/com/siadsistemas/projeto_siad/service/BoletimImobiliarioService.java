package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.BoletimImobiliarioDTO;
import com.siadsistemas.projeto_siad.model.BoletimImobiliario;
import com.siadsistemas.projeto_siad.repository.BoletimImobiliarioRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public List<BoletimImobiliario> findAll() {
        return boletimRepository.findAllByAtivoTrue();
    }

    @Transactional
    public BoletimImobiliario create(BoletimImobiliarioDTO dto) {
        if (boletimRepository.existsByMatricula(dto.matricula())) {
            throw new IllegalArgumentException("Já existe um boletim com esta matrícula.");
        }

        validarCampos(dto);

        BoletimImobiliario bci = new BoletimImobiliario();
        bci.setCodigo(boletimRepository.findMaxCodigo() + 1);
        bci.setMatricula(dto.matricula());
        bci.setResponsavelLegal(dto.responsavelLegal());
        bci.setEnderecoFisico(dto.enderecoFisico());
        bci.setEnderecoCorrespondencia(dto.enderecoCorrespondencia());
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

        existente.setMatricula(dto.matricula());
        existente.setResponsavelLegal(dto.responsavelLegal());
        existente.setEnderecoFisico(dto.enderecoFisico());
        existente.setEnderecoCorrespondencia(dto.enderecoCorrespondencia());
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
}
