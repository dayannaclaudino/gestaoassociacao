package com.api.gestaoassociacao.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.enums.StatusAssociado;


@Repository
@Transactional
public interface AssociadoRepository extends JpaRepository<Associado, Long>{
    
    Optional<Associado> findById(Long id);

    @Query("select a from Associado a where a.nome like %?1% ")
    public Page<Associado> findByNomeContaining(String nome, Pageable pageable);

    public Optional<Associado> findByCpf(String cpf);
    
    public Optional<Associado>  findByStatus(StatusAssociado status);
}