package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.ResponsavelLegalDTO;
import com.siadsistemas.projeto_siad.exception.domain.responsavelLegal.ResponsavelLegalException;
import com.siadsistemas.projeto_siad.exception.domain.responsavelLegal.ResponsavelLegalNotFoundException;
import com.siadsistemas.projeto_siad.model.Bairro;
import com.siadsistemas.projeto_siad.model.Endereco;
import com.siadsistemas.projeto_siad.model.Logradouro;
import com.siadsistemas.projeto_siad.model.ResponsavelLegal;
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
public class ResponsavelLegalService {

    private final ResponsavelLegalRepository responsavelLegalRepository;
    private final EnderecoRepository enderecoRepository;
    private final LogradouroService logradouroService;
    private final BairroService bairroService;

    public List<ResponsavelLegal> findAll() {
        return responsavelLegalRepository.findAllByAtivoTrueOrderByCodigoAsc();
    }

    @Transactional
    public ResponsavelLegal create(ResponsavelLegalDTO dto) {
        validarCampos(dto);
        validarUnicidade(dto);

        ResponsavelLegal responsavel = new ResponsavelLegal();
        responsavel.setCodigo(responsavelLegalRepository.findMaxCodigo() + 1);
        responsavel.setTipoPessoa(dto.tipoPessoa());
        responsavel.setNome(dto.nome());
        responsavel.setTelefoneFixo(dto.telefoneFixo());
        responsavel.setTelefoneCelular(dto.telefoneCelular());
        responsavel.setEmail(dto.email());
        responsavel.setNumeroDocumento(dto.numeroDocumento());

        Endereco endereco = buscarOuCriarEnderecoCompleto(dto.endereco());
        responsavel.setEndereco(endereco);

        responsavel.setAtivo(true);
        responsavel.setCreated_at(LocalDateTime.now());
        responsavel.setUpdated_at(LocalDateTime.now());

        return responsavelLegalRepository.save(responsavel);
    }

    @Transactional
    public ResponsavelLegal update(UUID id, ResponsavelLegalDTO dto) {
        ResponsavelLegal existente = responsavelLegalRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponsavelLegalNotFoundException("Responsável legal não encontrado com id: " + id));

        validarCampos(dto);
        validarUnicidade(dto);

        existente.setTipoPessoa(dto.tipoPessoa());
        existente.setNome(dto.nome());
        existente.setTelefoneFixo(dto.telefoneFixo());
        existente.setTelefoneCelular(dto.telefoneCelular());
        existente.setEmail(dto.email());
        existente.setNumeroDocumento(dto.numeroDocumento());

        Endereco endereco = buscarOuCriarEnderecoCompleto(dto.endereco());
        existente.setEndereco(endereco);

        existente.setUpdated_at(LocalDateTime.now());

        return responsavelLegalRepository.save(existente);
    }

    @Transactional
    public void inativarPorId(UUID id) {
        ResponsavelLegal existente = responsavelLegalRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponsavelLegalNotFoundException("Responsável legal não encontrado com id: " + id));

        existente.setAtivo(false);
        existente.setUpdated_at(LocalDateTime.now());
        responsavelLegalRepository.save(existente);
    }

    private void validarCampos(ResponsavelLegalDTO dto) {
        if (dto.tipoPessoa() == null) {
            throw new ResponsavelLegalException("Tipo de pessoa é obrigatório.");
        }
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new ResponsavelLegalException("Nome é obrigatório.");
        }
        if (dto.numeroDocumento() == null || dto.numeroDocumento().isBlank()) {
            throw new ResponsavelLegalException("Número do documento é obrigatório.");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new ResponsavelLegalException("E-mail é obrigatório.");
        }
    }

    public void validarUnicidade(ResponsavelLegalDTO dto) {
        if (responsavelLegalRepository.existsByEmail(dto.email())) {
            throw new ResponsavelLegalException("E-mail já cadastrado.");
        }

        if (responsavelLegalRepository.existsByNumeroDocumentoAndTipoPessoa(dto.numeroDocumento(), dto.tipoPessoa())) {
            throw new ResponsavelLegalException("Documento já cadastrado para esse tipo de pessoa.");
        }
    }

    public Endereco buscarOuCriarEnderecoCompleto(Endereco enderecoDTO) {
        Optional<Endereco> endereco = enderecoRepository.findByCampos(
                enderecoDTO.getNumero(),
                enderecoDTO.getComplemento(),
                enderecoDTO.getCep(),
                enderecoDTO.getLogradouro().getNome(),
                enderecoDTO.getLogradouro().getTipo_logradouro().getCodigo(),
                enderecoDTO.getBairro().getNome(),
                enderecoDTO.getBairro().getCidade().getCodigo()
        );

        if (endereco.isPresent()) {
            return endereco.get();
        }

        Logradouro logradouro = logradouroService.buscarOuCriar(
                enderecoDTO.getLogradouro().getNome(),
                enderecoDTO.getLogradouro().getTipo_logradouro().getCodigo()
        );

        Bairro bairro = bairroService.buscarOuCriar(
                enderecoDTO.getBairro().getNome(),
                enderecoDTO.getBairro().getCidade().getCodigo()
        );

        Endereco novoEndereco = new Endereco();
        novoEndereco.setLogradouro(logradouro);
        novoEndereco.setBairro(bairro);
        novoEndereco.setNumero(enderecoDTO.getNumero());
        novoEndereco.setComplemento(enderecoDTO.getComplemento());
        novoEndereco.setCep(enderecoDTO.getCep());
        novoEndereco.setAtivo(true);
        novoEndereco.setCodigo(enderecoRepository.findMaxCodigo() + 1);
        novoEndereco.setCreated_at(LocalDateTime.now());
        novoEndereco.setUpdated_at(LocalDateTime.now());

        return enderecoRepository.save(novoEndereco);
    }

}