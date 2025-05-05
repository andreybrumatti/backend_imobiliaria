package com.siadsistemas.projeto_siad.dto;

import com.siadsistemas.projeto_siad.enums.TipoPessoaEnum;
import com.siadsistemas.projeto_siad.model.Endereco;

public record ResponsavelLegalDTO(
        TipoPessoaEnum tipoPessoa,
        String nome,
        String telefoneFixo,
        String telefoneCelular,
        String email,
        String numeroDocumento,
        Endereco endereco
){

}