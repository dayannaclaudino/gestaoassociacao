package com.api.gestaoassociacao.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;



@Repository
@Transactional
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long>{
 
    //pesquisa em qualquer parte da palavra e ordena a lista (listaMensalidades)
    @Query("select m from Mensalidade m where m.associado.id = ?1 order by m.parcela asc")	
    public List<Mensalidade> getMensalidades(Long associadoId);

    public Optional<Mensalidade> findById(Long id);

    //Mensalidades em Pendentes
    @Query("SELECT m FROM Mensalidade m WHERE m.associado.id = ?1 AND m.situacao != 'Pago' ORDER BY m.parcela ASC")
    List<Mensalidade> getMensalidadesPendentes(Long associadoId);

    //Total soma das mensalidades em aberto
    @Query("SELECT SUM(m.valor) FROM Mensalidade m WHERE m.situacao = 'Pendente'")
    public BigDecimal sumMensalidadesEmAberto();

    @Query("SELECT m FROM Mensalidade m WHERE "
        + "(:nome IS NULL OR m.associado.nome LIKE %:nome%) "
        + "AND (:situacao IS NULL OR m.situacao = :situacao) "
        + "AND (:dataDe IS NULL OR m.dataEmissao >= :dataDe) "
        + "AND (:dataAte IS NULL OR m.dataEmissao <= :dataAte)")
   Page<Mensalidade> findByAssociadoNomeContainingAndSituacaoAndDataEmissaoBetween(
        @Param("nome") String nome, 
        @Param("situacao") SituacaoMensalidade situacao, 
        @Param("dataDe") LocalDate dataDe, 
        @Param("dataAte") LocalDate dataAte, 
        Pageable pageable);

}
