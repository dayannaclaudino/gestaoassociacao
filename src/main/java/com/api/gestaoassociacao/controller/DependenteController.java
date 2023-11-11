package com.api.gestaoassociacao.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.api.gestaoassociacao.model.Dependente;
import com.api.gestaoassociacao.service.AssociadoService;
import com.api.gestaoassociacao.service.DependenteService;

import jakarta.validation.Valid;

@Controller
public class DependenteController {

    private static final String VIEW = "cadastroDependentes";

    @Autowired
    private DependenteService dependenteService;

    @Autowired
    private AssociadoService associadoService;



    @GetMapping("/dependentes")
    public ModelAndView getDependentes(){
        
        ModelAndView mv = new ModelAndView(VIEW);
    
        mv.addObject("dependente",  new Dependente());
        mv.addObject("dependentes", dependenteService.getDependentes());
        mv.addObject("associados", associadoService.getAssociados());
        
        return mv;
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Dependente dependente){

        dependenteService.inserir(dependente);
        return "redirect:/dependentes";
    }

   

}