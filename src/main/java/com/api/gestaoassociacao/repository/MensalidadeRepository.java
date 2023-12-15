package com.api.gestaoassociacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.gestaoassociacao.model.Mensalidade;



@Repository
@Transactional
public interface MensalidadeRepository extends JpaRepository<Mensalidade, Long>{
 
    @Query("select m from Mensalidade m where m.associado.id = ?1")	
    public List<Mensalidade> getMensalidades(Long associadoId);

    public Optional<Mensalidade> findById(Long id);
}
