package com.api.gestaoassociacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Dependente;
import com.api.gestaoassociacao.repository.AssociadoRepository;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    public List<Associado> getAssociados(){
        return associadoRepository.findAll();
    }

    public void inserir(Associado associado){
        associadoRepository.save(associado);
    }

    public Associado alterar(Associado associado){
        return associadoRepository.saveAndFlush(associado);
    }

    public void remover(Long id){
        Associado associado = associadoRepository.findById(id).get();
        associadoRepository.delete(associado);
    }


    public Associado findById(Long associadoId) {
        return associadoRepository.findById(associadoId).get();
    }

}

