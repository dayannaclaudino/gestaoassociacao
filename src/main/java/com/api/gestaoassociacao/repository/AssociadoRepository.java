package com.api.gestaoassociacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.gestaoassociacao.model.Associado;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long>{
    
}