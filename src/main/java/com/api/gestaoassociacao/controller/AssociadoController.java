package com.api.gestaoassociacao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.Exception.*;
import com.api.gestaoassociacao.repository.filter.AssociadoFilter;
import com.api.gestaoassociacao.service.AssociadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api")
public class AssociadoController {

    private static final String VIEW = "cadastroAssociado";

    @Autowired
    private AssociadoService associadoService;


    @RequestMapping("/novo")
    public ModelAndView clicaNovoAssociado(Associado associado) {
        return new ModelAndView(VIEW);
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Associado associado, BindingResult result){
        if (result.hasErrors()) {
            return clicaNovoAssociado(associado);
        } 
         associadoService.inserir(associado);
        return new ModelAndView("redirect:/api/novo");
    }



    @RequestMapping
    public ModelAndView adicao(@PathVariable Long id){
        System.out.println("peguei o id..." + id);
        ModelAndView mv = new ModelAndView("redirect:/api/novo");
      
        return mv;
    }

    @DeleteMapping(value = "{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            associadoService.remover(id);
        }catch (NegocioException e){
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/api/pesquisar";
        }

        attributes.addFlashAttribute("mensagem", "Associado excluído com sucesso.");
        return "redirect:/api/pesquisar";
    }    

    @RequestMapping("/pesquisar")
	public ModelAndView pesquisar(@ModelAttribute("filtro") AssociadoFilter filtro) {
 		List<Associado> todosAssociados = associadoService.filtrar(filtro);
		
		ModelAndView mv = new ModelAndView("listaAssociados");
		mv.addObject("associados", todosAssociados);
		return mv;
	}
}

