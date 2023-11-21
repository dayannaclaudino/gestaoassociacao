package com.api.gestaoassociacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.repository.AssociadoRepository;
import com.api.gestaoassociacao.repository.filter.AssociadoFilter;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    public List<Associado> getAssociados(){
        return associadoRepository.findAll();
    }

    public void salvar(Associado associado){
        try {
            associadoRepository.save(associado);
        } catch (DataIntegrityViolationException  e) {
            throw new IllegalArgumentException("Formato de data inválido");
        }     
    }

    public void remover(Long id){
        Associado associado = associadoRepository.findById(id).get();
        associadoRepository.delete(associado);
    }


    public Associado findById(Long associadoId) {
        return associadoRepository.findById(associadoId).get();
    }

    public List<Associado> filtrar(AssociadoFilter filtro){
        String nomeAssociado = filtro.getNome() == null ? "%" : filtro.getNome();
		return associadoRepository.findByNomeContaining(nomeAssociado);
	}

     
    public Page<Associado> findAllPageable(Pageable pageable) {
        return associadoRepository.findAll(pageable);

    }

}