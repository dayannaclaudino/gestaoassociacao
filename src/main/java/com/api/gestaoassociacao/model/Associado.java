package com.api.gestaoassociacao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.api.gestaoassociacao.model.enums.StatusAssociado;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    
    @NotBlank(message="Esse campo é obrigatório!")
    private String nome;
    private String apelido;


    @NotBlank(message="Esse campo é obrigatório!")
    @CPF(message = "Cpf inválido, tente um cpf válido.")
    private String cpf;

    private String rg;
    private String nis;
    private String tituloEleitor;

    private String nomeMae;
    private String nomePai;
    private String celular;
    private String celular2;

    
    private String observacao;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCadastro;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate socioDesde;
    
    @Enumerated(EnumType.STRING)
    private StatusAssociado status;

    @OneToMany(mappedBy="associado", cascade = CascadeType.ALL)
    private List<Dependente> dependentes;  

    @OneToMany(mappedBy="associado",  cascade = CascadeType.ALL)
    private List<Mensalidade> mensalidades;  
}

