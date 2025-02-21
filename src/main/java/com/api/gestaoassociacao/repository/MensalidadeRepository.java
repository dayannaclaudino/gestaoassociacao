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

import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;



@Repository
@Transactional
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long>{
 
    //pesquisa em qualquer parte da palavra e ordena a lista
    @Query("select m from Mensalidade m where m.associado.id = ?1 order by m.parcela DESC")	
    public List<Mensalidade> getMensalidades(Long associadoId);

    public Optional<Mensalidade> findById(Long id);

    //Não listar Mensalidades pagas cadastromensalidade
    @Query("SELECT m FROM Mensalidade m WHERE m.associado.id = ?1 AND m.situacao != 'Pago' ORDER BY m.dataEmissao DESC")
    List<Mensalidade> getMensalidadesPendentes(Long associadoId);

    //Total soma das mensalidades em aberto (home)
    @Query("SELECT SUM(m.valor) FROM Mensalidade m WHERE m.situacao != 'Pago'")
    public BigDecimal sumMensalidadesEmAberto();

    //Filtro de todas as mensalidades com paginação
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

    //Filtro de todas as mensalidades sem paginação
     @Query("SELECT m FROM Mensalidade m " +
        "WHERE (:nomeAssociado IS NULL OR m.associado.nome LIKE %:nomeAssociado%) " +
        "AND (:situacao IS NULL OR m.situacao = :situacao) " +
        "AND (:inicio IS NULL OR :fim IS NULL OR m.dataEmissao BETWEEN :inicio AND :fim)")
    List<Mensalidade> buscarTodasMensalidades(@Param("nomeAssociado") String nomeAssociado,
                                           @Param("situacao") SituacaoMensalidade situacao,
                                           @Param("inicio") LocalDate inicio,
                                           @Param("fim") LocalDate fim);    

    List<Mensalidade> findByOrderByDataEmissaoDesc();

}
