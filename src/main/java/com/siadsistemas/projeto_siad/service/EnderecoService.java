package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.EnderecoDTO;
import com.siadsistemas.projeto_siad.exception.domain.endereco.EnderecoException;
import com.siadsistemas.projeto_siad.exception.domain.endereco.EnderecoNotFoundException;
import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.model.Endereco;
import com.siadsistemas.projeto_siad.model.Logradouro;
import com.siadsistemas.projeto_siad.repository.EnderecoRepository;
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
    private final LogradouroService logradouroService;
    private final BairroService bairroService;

    public List<Endereco> findAll() {
        return enderecoRepository.findAllByAtivoTrueOrderByCodigoAsc();
    }

    @Transactional
    public Endereco create(EnderecoDTO dto) {
        validarCamposParaCriacao(dto);

        Logradouro logradouro = logradouroService.buscarOuCriar(dto.logradouro().getNome(), dto.logradouro().getTipo_logradouro().getCodigo());

        Bairro bairro = bairroService.buscarOuCriar(dto.endereco().getBairro().getNome(), dto.endereco().getBairro().getCidade().getCodigo());

        Endereco endereco = new Endereco();
        endereco.setCodigo(enderecoRepository.findMaxCodigo() + 1);
        endereco.setLogradouro(logradouro);
        endereco.setBairro(bairro);
        endereco.setNumero(dto.endereco().getNumero());
        endereco.setComplemento(dto.endereco().getComplemento());
        endereco.setCep(dto.endereco().getCep());

        return enderecoRepository.save(endereco);
    }

    @Transactional
    public Endereco update(EnderecoDTO dto) {
        Endereco existente = enderecoRepository.findByIdAndAtivoTrue(dto.endereco().getId())
                .orElseThrow(() -> new EnderecoNotFoundException("Endereço não encontrado com ID: " + dto.endereco().getId()));

        Logradouro logradouro = logradouroService.buscarOuCriar(dto.logradouro().getNome(), dto.logradouro().getTipo_logradouro().getCodigo());

        Bairro bairro = bairroService.buscarOuCriar(dto.endereco().getBairro().getNome(), dto.endereco().getBairro().getCidade().getCodigo());

        validarCamposParaAtualizacao(dto);

        existente.setLogradouro(logradouro);
        existente.setBairro(bairro);
        existente.setNumero(dto.endereco().getNumero());
        existente.setComplemento(dto.endereco().getComplemento());
        existente.setCep(dto.endereco().getCep());

        return enderecoRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        Endereco existente = enderecoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EnderecoNotFoundException("Endereço não encontrado com ID: " + id));

        existente.setAtivo(false);

        enderecoRepository.save(existente);
    }

    private void validarCamposParaCriacao(EnderecoDTO dto) {
        if (dto.endereco().getNumero() == null || dto.endereco().getCep() == null) {
            throw new EnderecoException("Número e CEP são obrigatórios.");
        }
    }

    private void validarCamposParaAtualizacao(EnderecoDTO dto) {
        if (dto.endereco() == null || dto.endereco().getId() == null) {
            throw new EnderecoException("Código do endereço é obrigatório para atualização.");
        }
        validarCamposParaCriacao(dto);
    }
}
