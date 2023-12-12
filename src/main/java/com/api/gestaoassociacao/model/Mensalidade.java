package com.api.gestaoassociacao.model;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class Mensalidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "associadoId")
    private Associado associado;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "dataEmissao")
    private LocalDate dataEmissao;

    @NotNull(message = "Date de vencimento é obrigatória")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataVencimento;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataPagamento;

    @NotNull
    @NumberFormat(pattern = "#,##0.00")
    @DecimalMin(value = "0.01", message = "Valor não pode ser menor que 0,01")
	@DecimalMax(value = "9999999.99", message = "Valor não pode ser maior que 9.999.999,99") 
    private BigDecimal valor;

    @NumberFormat(pattern = "#,##0.00") 
    @DecimalMin(value = "0.01", message = "Valor não pode ser menor que 0,01")
	@DecimalMax(value = "9999999.99", message = "Valor não pode ser maior que 9.999.999,99")
    @Column(name = "valorPago")
    private BigDecimal valorPago;

    private int parcela;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SituacaoMensalidade situacao;

}
