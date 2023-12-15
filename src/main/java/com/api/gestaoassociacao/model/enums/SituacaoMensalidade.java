package com.api.gestaoassociacao.model.enums;

public enum SituacaoMensalidade {
    
    Pendente("Pendente"),
    Pago("Pago");

    private String descricao;

    SituacaoMensalidade(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
