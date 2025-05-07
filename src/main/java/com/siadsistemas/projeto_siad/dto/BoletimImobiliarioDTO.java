package com.siadsistemas.projeto_siad.dto;

import com.siadsistemas.projeto_siad.model.Endereco;

public record BoletimImobiliarioDTO(
        String matricula,
        ResponsavelLegalDTO responsavelLegal,
        Endereco enderecoFisico,
        Endereco enderecoCorrespondencia) {

}