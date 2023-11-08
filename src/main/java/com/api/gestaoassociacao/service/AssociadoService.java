package com.api.gestaoassociacao.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.repository.AssociadoRepository;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    public List<Associado> buscarTodosAssociados(){
        return associadoRepository.findAll();
    }

    public void inserir(Associado associado){
        associadoRepository.save(associado);
    }

    public Associado alterar(Associado associado){
        return associadoRepository.saveAndFlush(associado);
    }

    public void excluir(Long id){
        Associado associado = associadoRepository.findById(id).get();
        associadoRepository.delete(associado);
    }

}

