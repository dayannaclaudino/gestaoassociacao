package com.api.gestaoassociacao.model;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "dependente")
@Getter
@Setter
@ToString
public class Dependente implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Esse campo é obrigatório!")
    private String nome;
    private String parentesco;

    @JoinColumn(name="associado_id")
    @ManyToOne
    private Associado associado;

    
}