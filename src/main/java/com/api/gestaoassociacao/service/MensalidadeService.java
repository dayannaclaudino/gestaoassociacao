package com.api.gestaoassociacao.service;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
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
	//Exibe a soma do valor de mensalidades em aberto na página home
	public BigDecimal getTotalMensalidadesEmAberto() {
        return mensalidadeRepository.sumMensalidadesEmAberto();
    }

	//lista de mensalidades pendentes (para tabela de cad de mensalidades em aberto)
	public List<Mensalidade> getMensalidadesPendentes(Long associadoId){
		return mensalidadeRepository.getMensalidadesPendentes(associadoId);
	}
	
	
    public BigDecimal getTotalMensalidadesEmAtraso() {
        List<Mensalidade> mensalidades = mensalidadeRepository.findAll();
		// Processar a lista de mensalidades utilizando a API de Streams
        return mensalidades.stream()
		// Filtrar mensalidades que estão em atraso e são pendentes
                .filter(m -> m.getDiasAtraso() > 0 && m.getSituacao() == SituacaoMensalidade.Pendente)
				// Mapear cada mensalidade para seu valor (BigDecimal)
                .map(Mensalidade::getValor)
				 // Somar todos os valores utilizando a operação de redução, O valor inicial da redução, que é zero.
				//BigDecimal::add: A operação de acumulação que soma dois valores de BigDecimal.
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

	 //public Page<Mensalidade> filtrar(Filter filtro, Pageable pageable){
       // String nomeAssociado = filtro.getNome() == null ? "%" : filtro.getNome();
	//	return mensalidadeRepository.buscaAssociadoMensalidade(nomeAssociado, pageable);
	//}



}