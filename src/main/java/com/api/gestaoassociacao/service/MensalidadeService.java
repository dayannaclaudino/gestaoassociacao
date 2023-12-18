package com.api.gestaoassociacao.service;



import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
		
		mensalidade.setDataEmissao(LocalDate.now());
		mensalidadeRepository.save(mensalidade);
		
	}

	public void remover(Long id) {
		try {
			Mensalidade mensalidade = mensalidadeRepository.findById(id).get();

			if (mensalidade.getDataPagamento() == null) {
				mensalidadeRepository.deleteById(id);
			}else{
                throw new NegocioException("Mensalidade não pode ser removida, pois contém mensalidades pagas!");
            }

		} catch (DataIntegrityViolationException e) {
			throw new NegocioException("Mensalidade não pode ser removida, pois contém mensalidades pagas!");
		}
			
	}

	public Mensalidade getMensalidadeById(Long id){
		return mensalidadeRepository.findById(id).get();
	}

	public List<Mensalidade> getMensalidades(){
		return mensalidadeRepository.findAll();
	}

	 //public Page<Mensalidade> filtrar(Filter filtro, Pageable pageable){
       // String nomeAssociado = filtro.getNome() == null ? "%" : filtro.getNome();
	//	return mensalidadeRepository.buscaAssociadoMensalidade(nomeAssociado, pageable);
	//}

	

/*	public String receber(Long id) {
		Mensalidade mensalidade = mensalidadeRepository.findById(id).get();
		mensalidade.setSituacao(SituacaoMensalidade.Pago);
		mensalidadeRepository.save(mensalidade);
		
		return SituacaoMensalidade.Pago.getDescricao();
	} */

}