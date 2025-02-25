package com.api.gestaoassociacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.gestaoassociacao.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    
    Optional<Role> findByName(String name);
    
}
