package com.api.gestaoassociacao.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
import com.api.gestaoassociacao.repository.MensalidadeRepository;


@Service
public class MensalidadeService {

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

	
	public void salvar(Mensalidade mensalidade) {
		try {
			mensalidadeRepository.save(mensalidade);
		} catch (NegocioException e) {
			throw new NegocioException("Formato de data inválido");
		}
	}

	public void remover(Long id) {
		mensalidadeRepository.deleteById(id);		
	}

	public Mensalidade getMensalidadeById(Long id){
		return mensalidadeRepository.findById(id).get();
	}

	 //public Page<Mensalidade> filtrar(Filter filtro, Pageable pageable){
       // String nomeAssociado = filtro.getNome() == null ? "%" : filtro.getNome();
	//	return mensalidadeRepository.buscaAssociadoMensalidade(nomeAssociado, pageable);
	//}

	public List<Mensalidade> getMensalidades(){
		return mensalidadeRepository.findAll();
	}

	public String receber(Long id) {
		Mensalidade mensalidade = mensalidadeRepository.findById(id).get();
		mensalidade.setSituacao(SituacaoMensalidade.Pago);
		mensalidadeRepository.save(mensalidade);
		
		return SituacaoMensalidade.Pago.getDescricao();
	}

}