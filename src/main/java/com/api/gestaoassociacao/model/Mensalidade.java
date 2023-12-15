package com.api.gestaoassociacao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
import com.api.gestaoassociacao.model.enums.Tipo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "mensalidade")
@Getter
@Setter
@ToString
public class Mensalidade implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  
    @JoinColumn(name="associado_id")
    @ManyToOne
    private Associado associado;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String dataEmissao;

    @NotNull(message = "Date de vencimento é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String dataVencimento;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String dataPagamento;

    @NotNull
    @NumberFormat(pattern = "#,##0.00")
    @DecimalMin(value = "0.01", message = "Valor não pode ser menor que 0,01")
	@DecimalMax(value = "9999999.99", message = "Valor não pode ser maior que 9.999.999,99") 
    private BigDecimal valor;

    @NotNull(message = "Esse campo é obrigatório!")
    @DecimalMin(value = "1", message = "Valor não pode ser zero ou menor que zero.")
    private int parcela;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotNull(message = "Esse campo é obrigatório!")
    @Enumerated(EnumType.STRING)
    private SituacaoMensalidade situacao;

    public boolean isPendente(){
        return SituacaoMensalidade.Pendente.equals(this.situacao);
    }

}
