package com.api.gestaoassociacao.model.enums;

public enum SituacaoMensalidade {
    
    Pendente("PENDENTE"),
    Atrasado("ATRASADO"),
    Pago("PAGO");

    private String descricao;

    SituacaoMensalidade(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
