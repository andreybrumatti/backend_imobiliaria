package com.siadsistemas.projeto_siad.dto;

import com.siadsistemas.projeto_siad.model.Endereco;
import com.siadsistemas.projeto_siad.model.ResponsavelLegal;

public record BoletimImobiliarioDTO(String matricula, ResponsavelLegal responsavelLegal, Endereco enderecoFisico, Endereco enderecoCorrespondencia) {

}