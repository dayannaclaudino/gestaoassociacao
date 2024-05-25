package com.api.gestaoassociacao.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.StatusAssociado;
import com.api.gestaoassociacao.repository.AssociadoRepository;
import com.api.gestaoassociacao.repository.MensalidadeRepository;
import com.api.gestaoassociacao.repository.filter.Filter;

import jakarta.transaction.Transactional;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

    
    @Transactional
    public void salvar(Associado associado){
        Optional<Associado> buscaPorCpf = associadoRepository.findByCpf(associado.getCpf());

        if (buscaPorCpf.isPresent()) {
            throw new NegocioException("O CPF já está cadastrado no sistema.");
        }
        
        try {
            associado.setDataCadastro(LocalDate.now());
            associado.setStatus(StatusAssociado.valueOf("Ativo"));
            associadoRepository.save(associado);
        } catch (DataIntegrityViolationException e) {
            throw new NegocioException("Não foi possível concluir o cadastro. Informe Ao administrador do sistema!");
        }
    }

    @Transactional
    public void editar(Associado associado){

        associadoRepository.save(associado);
       
    }

    public void remover(Long id){
        try {
            Associado associado = associadoRepository.findById(id).get();
            if (associado.getDependentes().isEmpty() && associado.getMensalidades().isEmpty()) {
                associadoRepository.delete(associado);
            }else{
                throw new NegocioException("Associado não pode ser removido, pois contém dependentes ou mensalidades vinculados!");
            }
            
         }catch (DataIntegrityViolationException e){
            throw new NegocioException("Associado não pode ser removido, pois contém dependentes ou mensalidades vinculados!");
        }
    }

    public List<Associado> getAssociados(){
        return associadoRepository.findAll();
    }
    
    public Page<Associado> filtrar(Filter filtro, Pageable pageable){
        String nomeAssociado = filtro.getNome() == null ? "%" : filtro.getNome();
		return associadoRepository.findByNomeContaining(nomeAssociado, pageable);
	}

    public Optional<Associado> findById(Long id) {
        Optional<Associado> associado = associadoRepository.findById(id);
        return associado;
    }


}