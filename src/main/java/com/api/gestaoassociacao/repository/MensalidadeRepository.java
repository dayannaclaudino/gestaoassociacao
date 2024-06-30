package com.api.gestaoassociacao.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.gestaoassociacao.model.Mensalidade;



@Repository
@Transactional
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long>{
 
    //pesquisa em qualquer parte da palavra e ordena a lista (listaMensalidades)
    @Query("select m from Mensalidade m where m.associado.id = ?1 order by m.parcela asc")	
    public List<Mensalidade> getMensalidades(Long associadoId);

    public Optional<Mensalidade> findById(Long id);

    @Query("SELECT m FROM Mensalidade m WHERE m.associado.id = ?1 AND m.situacao != 'Pago' ORDER BY m.parcela ASC")
    List<Mensalidade> getMensalidadesPendentes(Long associadoId);

    //Total soma das mensalidades em aberto
    @Query("SELECT SUM(m.valor) FROM Mensalidade m WHERE m.situacao = 'Pendente'")
    public BigDecimal sumMensalidadesEmAberto();
}
