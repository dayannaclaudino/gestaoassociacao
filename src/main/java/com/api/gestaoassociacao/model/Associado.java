package com.api.gestaoassociacao.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.springframework.format.annotation.DateTimeFormat;

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

    @CPF
    @NotBlank(message="Esse campo é obrigatório!")
    private String cpf;

    private String rg;
    private String nis;
    @TituloEleitoral
    private String tituloEleitor;

    private String nomeMae;
    private String nomePai;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    private String celular;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicio;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate socioDesde;
    private String observacao;

    @OneToMany
    @JoinColumn(name="associado_id")
    private List<Dependente> dependentes;  
}

