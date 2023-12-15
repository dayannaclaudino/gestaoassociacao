package com.api.gestaoassociacao.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
import com.api.gestaoassociacao.model.enums.StatusAssociado;
import com.api.gestaoassociacao.model.enums.Tipo;
import com.api.gestaoassociacao.repository.MensalidadeRepository;
import com.api.gestaoassociacao.repository.AssociadoRepository;
import com.api.gestaoassociacao.service.AssociadoService;
import com.api.gestaoassociacao.service.MensalidadeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;




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
  
       
    @GetMapping("/listar")
    public ModelAndView getMensalidades() {

        ModelAndView mv = new ModelAndView("listaMensalidades");

        mv.addObject("mensalidade", new Mensalidade());
        mv.addObject("mensalidades", mensalidadeService.getMensalidades());
        mv.addObject("associados", associadoService.getAssociados());
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        
        return mv;
    }

    @RequestMapping("/detalheAssociado/{codigo}")
    public ModelAndView mensalidadeAssociado(@PathVariable("codigo") Long id) {
        
         Optional<Associado> associado = associadoRepository.findById(id);

        ModelAndView mv = new ModelAndView("cadastroMensalidades");
        mv.addObject("associado", associado.get());
        mv.addObject("mensalidades", mensalidadeRepository.getMensalidades(id));
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        return mv;
    }

    @PostMapping("/addMensalAssociado/{codigo}")
    public ModelAndView addMensalAssociado(@Valid Mensalidade mensalidade, @PathVariable("codigo") Long id, 
                                          BindingResult result, RedirectAttributes attributes){
        Associado associado = associadoRepository.findById(id).get();

        if (result.hasErrors()) {
            return new ModelAndView("redirect:/mensalidades/detalheAssociado/"+ associado.getId());
        }                                                                         
    
        ModelAndView mv = new ModelAndView(VIEW);
        mensalidade.setAssociado(associado);                                        
        mensalidadeService.salvar(mensalidade); 

        mv.addObject("associado", associado);
        mv.addObject("mensalidade", mensalidade);
        mv.addObject("mensalidades", mensalidadeRepository.getMensalidades(id));
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());                                   
        return mv;
    }

   

    @GetMapping(value="/{mensalidadeId}/")
	public String excluir(@PathVariable Long mensalidadeId, RedirectAttributes attributes) {
        
        Associado associado = mensalidadeRepository.findById(mensalidadeId).get().getAssociado();
            
        mensalidadeService.remover(mensalidadeId);
        ModelAndView mv = new ModelAndView(VIEW);
        mv.addObject("associado", associado);
        mv.addObject("mensalidades", mensalidadeRepository.getMensalidades(associado.getId()));
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        attributes.addFlashAttribute("mensagemSucesso",  "Mensalidade excluída com sucesso!");
        return "redirect:/mensalidades/detalheAssociado/"+ associado.getId();
		
	} 

   @GetMapping("/editarMensalidade")
    public ModelAndView editarMensalidade(@RequestParam Long  id) {

        ModelAndView mv = new ModelAndView("alterarMensalidade");
        
        Mensalidade mensalidade = mensalidadeService.getMensalidadeById(id);
        mv.addObject("mensalidade", mensalidade);
        mv.addObject("associados", associadoService.getAssociados());
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasSituacoes", SituacaoMensalidade.values());
        return mv;
    }

    @PostMapping("/salvarMensalidade")
    public String salvar(@ModelAttribute Mensalidade mensalidade, Associado associado){                                                                        
        
        mensalidadeService.salvar(mensalidade);
        mensalidade.setAssociado(associado);            
                                 
        return "redirect:/associados/listar";
    }
   
        
    
}
