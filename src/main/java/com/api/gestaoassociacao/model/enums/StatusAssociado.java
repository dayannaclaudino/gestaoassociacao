package com.api.gestaoassociacao.model.enums;

public enum StatusAssociado {

    ATIVO("Ativo"),
    INATIVO("Inativo");

    private String descricao;

    StatusAssociado(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
