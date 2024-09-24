package com.api.gestaoassociacao.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
import com.api.gestaoassociacao.model.enums.Tipo;
import com.api.gestaoassociacao.repository.MensalidadeRepository;
import com.api.gestaoassociacao.repository.filter.FilterMensalidade;
import com.api.gestaoassociacao.repository.AssociadoRepository;
import com.api.gestaoassociacao.service.AssociadoService;
import com.api.gestaoassociacao.service.MensalidadeService;

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
  
    //Lista todas as mensalidades do associado 'historicoMensalidades'
    @RequestMapping("/listar")
    public ModelAndView getMensalidades(@ModelAttribute("filtro") FilterMensalidade filtro, 
                               @PageableDefault(size = 9) Pageable pageable) {

        Page<Mensalidade> TodasMensalidades = mensalidadeService.filtrar(filtro, pageable);
        //Acho que isso está fazendo contar somente o que mostra
        List<Mensalidade> listaMensalidades = TodasMensalidades.getContent(); 
        BigDecimal totalParcelas = mensalidadeService.calcularTotalParcelas(listaMensalidades);
        
        ModelAndView mv = new ModelAndView("historicoMensalidades");

        mv.addObject("mensalidade", new Mensalidade());
        mv.addObject("mensalidades", TodasMensalidades);
        mv.addObject("associados", associadoService.getAssociados());
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        mv.addObject("totalParcelas", totalParcelas);
        return mv;
    }

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

    @PostMapping("/addMensalAssociado/{codigo}")
    public ModelAndView addMensalAssociado(@Valid Mensalidade mensalidade, BindingResult result, 
                                           @PathVariable("codigo") Long id, RedirectAttributes attributes){
        Associado associado = associadoRepository.findById(id).get();

        if (result.hasErrors()) {
            return mensalidadeAssociado(id, mensalidade);
        }                                                                         
        try {
            ModelAndView mv = new ModelAndView(VIEW);
            mensalidade.setAssociado(associado);                                        
            mensalidadeService.salvar(mensalidade); 
            mv.addObject("associado", associado);
            mv.addObject("mensalidades", mensalidadeRepository.getMensalidades(id));
            mv.addObject("todosTipos", Tipo.values());
            mv.addObject("todasSituacoes", SituacaoMensalidade.values());    
            attributes.addFlashAttribute("mensagemSucesso", "Mensalidade salva com sucesso.");
        return new ModelAndView("redirect:/mensalidades/detalheAssociado/"+ associado.getId());

        } catch (NegocioException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            System.out.println(e.getMessage());
            return mensalidadeAssociado(id, mensalidade);
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

   @GetMapping("/editarMensalidade")
    public ModelAndView editarMensalidade(@RequestParam Long  id) {

        ModelAndView mv = new ModelAndView("alterarMensalidade");
        
        Mensalidade mensalidade = mensalidadeService.getMensalidadeById(id);
        mv.addObject("mensalidade", mensalidade);
        mv.addObject("associado", associadoService.getAssociados());
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        return mv;
    }

    @PostMapping("/salvarMensalidade")
    public ModelAndView salvar(@Valid @ModelAttribute Mensalidade mensalidade, BindingResult result, RedirectAttributes attributes){                                                                        
             Associado associado = mensalidadeRepository.findById(mensalidade.getId()).get().getAssociado();
        if (result.hasErrors()) {
          return editarMensalidade(mensalidade.getId());
        }
        mensalidadeService.salvar(mensalidade);
        attributes.addFlashAttribute("mensagemSucesso", "Mensalidade alterada com sucesso.");                                     
        return new ModelAndView ("redirect:/mensalidades/detalheAssociado/"+ associado.getId());
    }
   
        
    
}
