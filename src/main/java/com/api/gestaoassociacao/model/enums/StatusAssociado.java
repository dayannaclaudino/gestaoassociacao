package com.api.gestaoassociacao.model.enums;

public enum StatusAssociado {

    Ativo("Ativo"),
    Inativo("Inativo");

    private String descricao;

    StatusAssociado(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
}
