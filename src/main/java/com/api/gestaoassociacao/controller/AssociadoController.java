package com.api.gestaoassociacao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.service.AssociadoService;

@Controller
@RequestMapping("/api")
public class AssociadoController {

    private static final String VIEW = "/associado";

    @Autowired
    private AssociadoService associadoService;

    @RequestMapping("/associado")
    public ModelAndView novo(Associado associado) {
        return new ModelAndView(VIEW);
    }

    @RequestMapping(value = "/associado", method = RequestMethod.POST)
    public ModelAndView salvar(Associado associado, RedirectAttributes attributes){
         associadoService.inserir(associado);
    
        attributes.addFlashAttribute("mensagem", "Entidade salva com sucesso.");
        return new ModelAndView("redirect:/api/associado");
    }

   /*   @GetMapping("/associados")
    public List<Associado> buscarAssociados(){
        return associadoService.buscarTodosAssociados();

    }

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

