package com.api.gestaoassociacao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.service.AssociadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api-associado")
public class AssociadoController {

    private static final String VIEW = "cadastroAssociado";

    @Autowired
    private AssociadoService associadoService;

    @RequestMapping("/novo")
    public ModelAndView clicaNovoAssociado(Associado associado) {
        return new ModelAndView(VIEW);
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Associado associado, BindingResult result, RedirectAttributes attributes){
        if (result.hasErrors()) {
            return clicaNovoAssociado(associado);
        } 
         associadoService.inserir(associado);
    
        attributes.addFlashAttribute("mensagem", "Associado salvo com sucesso.");
        return new ModelAndView("redirect:/api-associado/novo");
    }

    @GetMapping("/associados")
    public ModelAndView getAssociados(){
        ModelAndView mv = new ModelAndView("VIEW");
         mv.addObject("associados", associadoService.getAssociados()); 
         return mv;
    }
 /* 
    @PutMapping("{id}")
    public ModelAndView adicao(@PathVariable("id") Associado associado){
        ModelAndView mv = new ModelAndView(VIEW);
        mv.addObject(associado);
        return mv;
    }

    @DeleteMapping(value = "{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            associadoService.excluir(id);
        }catch (NegocioException e){
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/associados";
        }

        attributes.addFlashAttribute("mensagem", "Associado excluído com sucesso.");
        return "redirect:/associados";
    }*/
}

