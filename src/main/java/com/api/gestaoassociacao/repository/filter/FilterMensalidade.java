package com.api.gestaoassociacao.repository.filter;

import java.time.LocalDate;

import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;


public class FilterMensalidade {
    
    public String nomeAssociado;
    public SituacaoMensalidade situacao;
    public LocalDate dataDe;
    public LocalDate dataAte;
    
    public String getNomeAssociado() {
        return nomeAssociado;
    }
    public void setNomeAssociado(String nomeAssociado) {
        this.nomeAssociado = nomeAssociado;
    }
    public SituacaoMensalidade getSituacao() {
        return situacao;
    }
    public void setSituacao(SituacaoMensalidade situacao) {
        this.situacao = situacao;
    }
    public LocalDate getDataDe() {
        return dataDe;
    }
    public void setDataDe(LocalDate dataDe) {
        this.dataDe = dataDe;
    }
    public LocalDate getDataAte() {
        return dataAte;
    }
    public void setDataAte(LocalDate dataAte) {
        this.dataAte = dataAte;
    }

}
