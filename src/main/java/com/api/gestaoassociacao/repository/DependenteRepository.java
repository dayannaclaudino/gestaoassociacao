package com.api.gestaoassociacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.gestaoassociacao.model.Dependente;

@Repository
public interface DependenteRepository extends JpaRepository<Dependente, Long>{
    
}
