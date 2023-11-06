package com.api.gestaoassociacao.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "associados")
@Getter
@Setter
@ToString
public class Associado implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String apelido;
    private String cpf;
    private String rg;
    private String nis;
    private String tituloEleitor;

    private String nomeMae;

    private String nomePai;

    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    private String celular;
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    @Temporal(TemporalType.DATE)
    private Date socioDesde;
    private String observacao;

    @OneToMany(mappedBy = "associado", cascade = CascadeType.ALL)
    private List<Dependente> dependentes;  
}

