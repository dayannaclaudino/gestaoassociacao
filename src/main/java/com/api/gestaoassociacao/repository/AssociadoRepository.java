package com.api.gestaoassociacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.gestaoassociacao.model.Associado;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long>{
    
    Optional<Associado> findById(Long id);

    @Query("select a from Associado a where a.nome like %?1% ")
    public List<Associado> findByNomeContaining(String nome);
    
}