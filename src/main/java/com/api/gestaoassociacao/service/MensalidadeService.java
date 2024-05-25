package com.api.gestaoassociacao.service;



import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.repository.MensalidadeRepository;

import jakarta.transaction.Transactional;


@Service
public class MensalidadeService {

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

	@Transactional
	public void salvar(Mensalidade mensalidade) {
		
			if (mensalidade.getAssociado().getStatus().equalsIgnoreCase("Inativo")) {
				throw new NegocioException("Não é possível salvar mensalidades para associados inativos." 
												+" Altere o associado para ativo para incluir mensalidades." );
			}	
				mensalidade.setDataEmissao(LocalDate.now());
				mensalidadeRepository.save(mensalidade);
	}
	

	public void remover(Long id) {
	
			Mensalidade mensalidade = mensalidadeRepository.findById(id).orElseThrow(() -> 
				new NegocioException("Mensalidade não encontrada"));

			if (mensalidade.getSituacao().equals("Pendente")) {
				mensalidadeRepository.deleteById(id);
			}else{
                throw new NegocioException("A Mensalidade está paga e não pode ser removida!" +
				" Caso queira alterar os dados clique em editar.");
            }
	}

	@Transactional
	public void alterar(Mensalidade mensalidade) {
			mensalidadeRepository.save(mensalidade);	
	}

	public Mensalidade getMensalidadeById(Long id){
		return mensalidadeRepository.findById(id).get();
	}

	public List<Mensalidade> getMensalidades(){
		return mensalidadeRepository.findAll();
	}

}