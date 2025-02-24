package com.api.gestaoassociacao.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.controller.Exception.NegocioException;
import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
import com.api.gestaoassociacao.model.enums.Tipo;
import com.api.gestaoassociacao.repository.MensalidadeRepository;
import com.api.gestaoassociacao.repository.filter.FilterMensalidade;
import com.api.gestaoassociacao.repository.AssociadoRepository;
import com.api.gestaoassociacao.service.AssociadoService;
import com.api.gestaoassociacao.service.MensalidadeService;
import com.api.gestaoassociacao.service.Pdf.MensalidadePDFExporter;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/mensalidades")
public class MensalidadeController {
    
    private static final String VIEW = "cadastroMensalidades";

    @Autowired
    private MensalidadeService mensalidadeService;
    @Autowired
    private AssociadoRepository associadoRepository;
    @Autowired
    private MensalidadeRepository mensalidadeRepository;
    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private MensalidadePDFExporter mensalidadePDFExporter;
  
    
    //View Cadastro
    @RequestMapping("/detalheAssociado/{codigo}")
    public ModelAndView mensalidadeAssociado(@PathVariable("codigo") Long id, Mensalidade mensalidade) {
        
        Optional<Associado> associado = associadoRepository.findById(id);

        ModelAndView mv = new ModelAndView("cadastroMensalidades");
        mv.addObject("associado", associado.get());
        mv.addObject("mensalidades", mensalidadeRepository.getMensalidadesPendentes(id));
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        return mv;
    }

//Salvar mensalidade
    @PostMapping("/addMensalAssociado/{codigo}")
    public ModelAndView addMensalAssociado(@Valid Mensalidade mensalidade, BindingResult result, 
                                           @PathVariable("codigo") Long id, RedirectAttributes attributes){
        Associado associado = associadoRepository.findById(id).get();

        if (result.hasErrors()) {
            return mensalidadeAssociado(id, mensalidade);
        }                                                                         
        try {
            ModelAndView mv = new ModelAndView(VIEW);
            // Gerando um código único para o lote de mensalidades
            String codigoLote = gerarCodigoLote();

            mensalidade.setAssociado(associado); 
            mensalidade.setCodigoMensalidade(codigoLote); // Código único para o lote    
            mensalidade.setTotalParcelas(1); //sempre vai ser uma parcela                                   
            mensalidadeService.salvar(mensalidade); 
            mv.addObject("associado", associado);
            mv.addObject("mensalidades", mensalidadeRepository.getMensalidades(id));
            mv.addObject("todosTipos", Tipo.values());
            mv.addObject("todasSituacoes", SituacaoMensalidade.values());    
            attributes.addFlashAttribute("mensagemSucesso", "Mensalidade salva com sucesso.");
            return new ModelAndView("redirect:/mensalidades/detalheAssociado/"+ associado.getId());

        } catch (NegocioException e) {
            System.out.println(e.getMessage());
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            //return mensalidadeAssociado(id, mensalidade);
            return new ModelAndView("redirect:/mensalidades/detalheAssociado/"+ associado.getId());
        }
    } 
    
    /* */
   @GetMapping("/editarMensalidade")
    public ModelAndView editarFormulario(@RequestParam Long id) {

        ModelAndView mv = new ModelAndView("alterarMensalidade");
        
        Mensalidade mensalidade = mensalidadeService.getMensalidadeById(id);
        mv.addObject("mensalidade", mensalidade);
        mv.addObject("associado", associadoService.getAssociados());
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        return mv;
    }

//salvar editar 
    @PostMapping("/salvarMensalidade")
    public ModelAndView alterar(@Valid @ModelAttribute Mensalidade mensalidade, BindingResult result, RedirectAttributes attributes){                                                                        
        
        Associado associado = mensalidadeRepository.findById(mensalidade.getId()).get().getAssociado();

        if (result.hasErrors()) {
          return editarFormulario(mensalidade.getId());
        }
        try {
            mensalidadeService.salvar(mensalidade);
            attributes.addFlashAttribute("mensagemSucesso", "Mensalidade alterada com sucesso.");                                     
            return new ModelAndView ("redirect:/mensalidades/detalheAssociado/"+ associado.getId());
        } catch (NegocioException e) {
            ModelAndView mv = editarFormulario(mensalidade.getId());
            mv.addObject("mensagemErro", e.getMessage());
            return mv;
        }
    }
   

    @GetMapping(value="/{mensalidadeId}/")
	public String excluir(@PathVariable Long mensalidadeId, RedirectAttributes attributes) {
        
        Associado associado = mensalidadeRepository.findById(mensalidadeId).get().getAssociado();
        try{    
            mensalidadeService.remover(mensalidadeId);
            ModelAndView mv = new ModelAndView(VIEW);
            mv.addObject("associado", associado);
            mv.addObject("mensalidades", mensalidadeRepository.getMensalidades(associado.getId()));
            mv.addObject("todosTipos", Tipo.values());
            mv.addObject("todasSituacoes", SituacaoMensalidade.values());
            attributes.addFlashAttribute("mensagemSucesso",  "Mensalidade excluída com sucesso!");
            return "redirect:/mensalidades/detalheAssociado/"+ associado.getId();

        }catch(NegocioException e){
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/mensalidades/detalheAssociado/"+ associado.getId();   
        }	
	} 

    //historicoMensalidades
    @RequestMapping("/listar")
    public ModelAndView getMensalidades(@ModelAttribute("filtro") FilterMensalidade filtro, 
                               @PageableDefault(size = 9) Pageable pageable) {

        Page<Mensalidade> TodasMensalidades = mensalidadeService.filtrar(filtro, pageable);
        
        // Obter todas as mensalidades sem paginação
        List<Mensalidade> todasMensalidades = mensalidadeService.todasMensalidadesSemPaginacao(filtro);
        BigDecimal totalParcelas = mensalidadeService.calcularTotalParcelas(todasMensalidades);
        
        ModelAndView mv = new ModelAndView("historicoMensalidades");

        mv.addObject("mensalidade", new Mensalidade());
        mv.addObject("mensalidades", TodasMensalidades);
        mv.addObject("associados", associadoService.getAssociados());
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        mv.addObject("totalParcelas", totalParcelas);
        return mv;
    }

  public double calcularMensalidadesEmAberto(){    
        List<Mensalidade> mensalidades = mensalidadeRepository.findAll();
        double soma = 0.0;

        for (Mensalidade mensalidade : mensalidades) {
            if (mensalidade.isPendente()) {
            }
        }
        return soma;
    }     

 
    @GetMapping(value = "/pdf")
    public ResponseEntity<byte[]> gerarPdfMensalidades(@ModelAttribute("filtro") FilterMensalidade filtro) {

        List<Mensalidade> todasMensalidades = mensalidadeService.todasMensalidadesSemPaginacao(filtro);
        BigDecimal totalParcelas = mensalidadeService.calcularTotalParcelas(todasMensalidades);

        byte[] pdf = mensalidadePDFExporter.gerarRelatorioMensalidades(todasMensalidades, totalParcelas);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "relatorio-mensalidades.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    //OPÇÃO DE GERAR MENSALIDADE AUTOMÁTICA
    @GetMapping("/novo")
    public ModelAndView exibirFormulario() {
        ModelAndView mv = new ModelAndView("novaMensalidade"); 
        mv.addObject("associados", associadoRepository.findAll());
        mv.addObject("mensalidades", mensalidadeRepository.findByOrderByDataEmissaoDesc());
        return mv;
    }

    @PostMapping("/gerar")
    public String gerarMensalidades(
            @RequestParam Long associadoId,
            @RequestParam BigDecimal valorTotal,
            @RequestParam int quantidadeParcelas,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate primeiraDataVencimento, 
            Model model, RedirectAttributes attributes) {
    
        Associado associado = associadoRepository.findById(associadoId)
                .orElseThrow(() -> new IllegalArgumentException("Associado não encontrado"));

        // Gerando um código único para o lote de mensalidades
        String codigoLote = gerarCodigoLote();
    
        BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(quantidadeParcelas), RoundingMode.HALF_UP);
    
        for (int i = 0; i < quantidadeParcelas; i++) {
            Mensalidade mensalidade = new Mensalidade();
            mensalidade.setAssociado(associado);
            mensalidade.setDataEmissao(LocalDate.now()); // Data da criação da mensalidade
            mensalidade.setDataVencimento(primeiraDataVencimento.plusMonths(i));
            mensalidade.setValor(valorParcela);
            mensalidade.setParcela(i + 1);
            mensalidade.setTotalParcelas(quantidadeParcelas);
            mensalidade.setSituacao(SituacaoMensalidade.Em_Aberto);
            mensalidade.setCodigoMensalidade(codigoLote); // Código único para o lote
    
            mensalidadeRepository.save(mensalidade);
            attributes.addFlashAttribute("mensagemSucesso", "Mensalidade salva com sucesso.");
            return "redirect:/mensalidades/novo";
            
        }
        ModelAndView mv = new ModelAndView("novaMensalidade"); 
        mv.addObject("associado", associado);
        mv.addObject("mensalidades", mensalidadeRepository.findByOrderByDataEmissaoDesc());
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());    
        attributes.addFlashAttribute("mensagemSucesso", "Mensalidade salva com sucesso.");
        return "novaMensalidade";
    }
    


    private String gerarCodigoLote() {
        return "M" + System.currentTimeMillis() % 1000 + "-" + (int) (Math.random() * 100);
    }

}
