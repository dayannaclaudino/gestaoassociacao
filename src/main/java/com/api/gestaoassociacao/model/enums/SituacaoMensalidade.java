package com.api.gestaoassociacao.model.enums;

public enum SituacaoMensalidade {
    
    Pago("Compensado"),
    NaoPago("Não Pago"),
    Cancelado("Cancelado");

    private String descricao;

    SituacaoMensalidade(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
