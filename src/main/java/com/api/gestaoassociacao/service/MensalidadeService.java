package com.api.gestaoassociacao.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.gestaoassociacao.controller.Exception.NegocioException;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
import com.api.gestaoassociacao.repository.MensalidadeRepository;
import com.api.gestaoassociacao.repository.filter.FilterMensalidade;

import jakarta.transaction.Transactional;


@Service
public class MensalidadeService {

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

	@Transactional
	public void salvar(Mensalidade mensalidade) {	
		if (mensalidade.getSituacao() == SituacaoMensalidade.Pago && mensalidade.getDataPagamento() == null) {
			throw new NegocioException("A data de pagamento deve ser preenchida ao selecionar a situação 'PAGO'.");
		}
		mensalidade.setDataEmissao(LocalDate.now());
		mensalidadeRepository.save(mensalidade);	
	}

	public void remover(Long id) {
		try {
			Mensalidade mensalidade = mensalidadeRepository.findById(id).get();

			if (mensalidade.getSituacao() == SituacaoMensalidade.Pago) {
				throw new NegocioException("Mensalidade está paga e não pode ser removida, clique no botão de editar caso queira alterar!");
			}else{
                mensalidadeRepository.deleteById(id);
            }
		} catch (DataIntegrityViolationException e) {
			throw new NegocioException("Mensalidade está paga e não pode ser removida, clique no botão de editar caso queira alterar!");
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

	//Listar somente mensalidades em Aberta e atrasadas cadastromensalidade
	public List<Mensalidade> getMensalidadesPendentes(Long associadoId){
		return mensalidadeRepository.getMensalidadesPendentes(associadoId);
	}
	//Exibe O total de mensalidades em Atraso
    public BigDecimal getTotalMensalidadesEmAtraso() {
        List<Mensalidade> mensalidades = mensalidadeRepository.findAll();
		// Processa a lista de mensalidades utilizando a API de Streams
        return mensalidades.stream()
		// Filtra mensalidades que estão em atraso 
                .filter(m -> m.getDiasAtraso() > 0 && m.getSituacao() == SituacaoMensalidade.Atrasado)
				// Mapear cada mensalidade para seu valor (BigDecimal)
                .map(Mensalidade::getValor)
				 // Somar todos os valores utilizando a operação de redução, O valor inicial da redução, que é zero.
				//BigDecimal::add: A operação de acumulação que soma dois valores de BigDecimal.
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalParcelas(List<Mensalidade> mensalidades) {
        return mensalidades.stream()
                .map(Mensalidade::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

	public Page<Mensalidade> filtrar(FilterMensalidade filtro, Pageable pageable) {
		String nomeAssociado = (filtro.getNomeAssociado() != null && !filtro.getNomeAssociado().isEmpty()) ? filtro.getNomeAssociado() : null;
		String codigoMensalidade = (filtro.getCodigoMensalidade() != null && !filtro.getCodigoMensalidade().isEmpty()) ? filtro.getCodigoMensalidade() : null;
		SituacaoMensalidade situacao = filtro.getSituacao();
		LocalDate dataDe = filtro.getDataDe();
		LocalDate dataAte = filtro.getDataAte();
		
		return mensalidadeRepository.findByFilters(nomeAssociado, codigoMensalidade, situacao, dataDe, dataAte, pageable);
	} 
	
	//buscar todas as mensalidades sem a paginação
    public List<Mensalidade> todasMensalidadesSemPaginacao(FilterMensalidade filtro) {
        return mensalidadeRepository.buscarTodasMensalidadesSemPaginacao(
            filtro.getNomeAssociado() != null ? filtro.getNomeAssociado() : null,
            filtro.getSituacao(),
            filtro.getDataDe(),
            filtro.getDataAte()
        );
    }

}