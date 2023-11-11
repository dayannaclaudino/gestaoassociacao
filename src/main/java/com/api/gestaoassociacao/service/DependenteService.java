package com.api.gestaoassociacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.model.Dependente;
import com.api.gestaoassociacao.repository.DependenteRepository;

@Service
public class DependenteService {

    @Autowired
    private DependenteRepository  dependenteRepository;

    public List<Dependente> getDependentes(){
        return dependenteRepository.findAll();
    }

    public void inserir(Dependente dependente){
        dependenteRepository.save(dependente);
    }

    public Dependente getDependenteById(Long id){
        return dependenteRepository.findById(id).get();
    }

    public void remover(Dependente dependente){
         dependenteRepository.delete(dependente);
    }
    
}
