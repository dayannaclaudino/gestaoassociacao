package com.api.gestaoassociacao.service;

import java.util.List;
import java.util.Optional;

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

    public Page<Associado> filtrar(AssociadoFilter filtro, Pageable pageable){
        String nomeAssociado = filtro.getNome() == null ? "%" : filtro.getNome();
		return associadoRepository.findByNomeContaining(nomeAssociado, pageable);
	}

    public Optional<Associado> findById(Long id) {
        Optional<Associado> associado = associadoRepository.findById(id);
        return associado;
    }

}