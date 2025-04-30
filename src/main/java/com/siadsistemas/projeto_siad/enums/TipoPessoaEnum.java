package com.siadsistemas.projeto_siad.enums;

public enum TipoPessoaEnum {
    PESSOA_FISICA(1, "Pessoa Física"),
    PESSOA_JURIDICA(2 , "Pessoa Jurídica");

    private final Integer codigo;
    private final String descricao;

    TipoPessoaEnum(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}