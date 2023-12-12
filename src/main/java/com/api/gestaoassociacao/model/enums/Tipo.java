package com.api.gestaoassociacao.model.enums;

public enum Tipo {
    Mensalidade("Mensalidade"),
    Saida("Saída");

    
    private String descricao;

    Tipo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
