package com.api.gestaoassociacao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    @Column(nullable = false, unique = true)
    private String codigoMensalidade;  // Código único para identificação
  
    @JoinColumn(name="associado_id")
    @ManyToOne
    private Associado associado;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
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

    @NotNull(message = "Obrigatório!")
    @DecimalMin(value = "1", message = "Valor inválido.")
    private int parcela;

    private Integer totalParcelas;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotNull(message = "Esse campo é obrigatório!")
    @Enumerated(EnumType.STRING)
    private SituacaoMensalidade situacao;

    public boolean isPendente(){
        return SituacaoMensalidade.Em_Aberto.equals(this.situacao);
    }

    //Calcula os dias em atraso
    public long getDiasAtraso() {
        if (dataVencimento == null) {
            return 0;
        }
        LocalDate dataReferencia = dataPagamento != null ? dataPagamento : LocalDate.now();
        
        if (dataReferencia.isBefore(dataVencimento)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataVencimento, dataReferencia);
    }
}
