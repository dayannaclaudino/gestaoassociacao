package com.api.gestaoassociacao.model;

import java.io.Serializable;

import jakarta.persistence.*;
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
    private int id;
    private String nome;
    private String parentesco;

    @ManyToOne(cascade = CascadeType.ALL)
    private Associado associado;
}