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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Dependente;
import com.api.gestaoassociacao.repository.AssociadoRepository;
import com.api.gestaoassociacao.repository.DependenteRepository;
import com.api.gestaoassociacao.service.AssociadoService;
import com.api.gestaoassociacao.service.DependenteService;


@Controller
@RequestMapping("dependentes")
public class DependenteController {

    private static final String VIEW = "cadastroDependentes";

    @Autowired
    private DependenteService dependenteService;
    @Autowired
    private DependenteRepository dependenteRepository;
    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private AssociadoRepository associadoRepository;




    @GetMapping("/listar")
    public ModelAndView getDependentes(){
        
       ModelAndView mv = new ModelAndView(VIEW);
    
        mv.addObject("dependente",  new Dependente());
        mv.addObject("dependentes", dependenteService.getDependentes());
        mv.addObject("associados", associadoService.getAssociados());
        
        return mv;
    }

    @RequestMapping("/detalheAssociado/{codigo}")
    public ModelAndView dependenteAssociado(@PathVariable("codigo") Long id) {
        
         Optional<Associado> associado = associadoRepository.findById(id);

        ModelAndView mv = new ModelAndView("cadastroDependentes");
        mv.addObject("associado", associado.get());
        mv.addObject("dependentes", dependenteRepository.getDependentes(id));
        return mv;
    }

    @PostMapping("/addDepenAssociado/{codigo}")
    public ModelAndView addDepenAssociado(Dependente dependente, @PathVariable("codigo") Long id, 
                                          BindingResult result, RedirectAttributes attributes){

        Associado associado = associadoRepository.findById(id).get();
    
        ModelAndView mv = new ModelAndView(VIEW);
        dependente.setAssociado(associado);                                        
        dependenteService.inserir(dependente); 

        mv.addObject("associado", associado);
        mv.addObject("dependentes", dependenteRepository.getDependentes(id));                                    
        return mv;
    }

    @GetMapping("/{dependenteId}/excluir")
	public ModelAndView excluir(@PathVariable("dependenteId") Long dependenteId) {
		
		Associado associado = dependenteRepository.findById(dependenteId).get().getAssociado();
		
		dependenteService.remover(dependenteId);
        ModelAndView mv = new ModelAndView(VIEW);
        mv.addObject("associado", associado);
        mv.addObject("dependentes", dependenteRepository.getDependentes(associado.getId()));
        return mv;
		
	}

    
}