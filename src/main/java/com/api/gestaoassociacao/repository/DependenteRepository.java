package com.api.gestaoassociacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.api.gestaoassociacao.model.Dependente;

@Repository
@Transactional
public interface DependenteRepository extends JpaRepository<Dependente, Long>{
    
   @Query("select d from Dependente d where d.associado.id = ?1")	
   public List<Dependente> getDependentes(Long id);
}
