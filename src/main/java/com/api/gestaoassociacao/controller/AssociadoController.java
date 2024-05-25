package com.api.gestaoassociacao.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.enums.StatusAssociado;
import com.api.gestaoassociacao.repository.filter.Filter;
import com.api.gestaoassociacao.service.AssociadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/associados")
public class AssociadoController {

    private static final String VIEW = "cadastroAssociado";

    @Autowired
    private AssociadoService associadoService;

    @RequestMapping("/novo")
    public ModelAndView novo(Associado associado) {
        ModelAndView mv = new ModelAndView(VIEW);
        mv.addObject(associado);
        mv.addObject("todosStatus", StatusAssociado.values());
 
        return mv;
    }

    @PostMapping(value = "/salvar")
    public ModelAndView salvar(@Valid Associado associado, BindingResult result, RedirectAttributes attributes, 
                                Model model) {
      
        if (result.hasErrors()) {
            return novo(associado);
        }
        try {
            associadoService.salvar(associado);
            attributes.addFlashAttribute("mensagemSucesso", "Associado '"+ associado.getNome() + "' salvo com sucesso.");
            return new ModelAndView("redirect:/dependentes/detalheAssociado/"+ associado.getId());
            
        } catch (NegocioException e) {
             model.addAttribute("mensagemErro", e.getMessage());
            System.out.println(e.getMessage());
            return novo(associado);
        }   
    }

    @RequestMapping("/listar")
    public ModelAndView listar(@ModelAttribute("filtro") Filter filtro, 
                               @PageableDefault(size = 9) Pageable pageable) {

        Page<Associado> todosAssociados = associadoService.filtrar(filtro, pageable);
        ModelAndView mv = new ModelAndView("listaAssociados");
        mv.addObject("associados", todosAssociados);
        mv.addObject("todosStatus", StatusAssociado.values());
        
        return mv;
    }


    @RequestMapping("/editar/{id}")
    public ModelAndView editarView(@PathVariable("id") Associado associado) {
        ModelAndView mv = new ModelAndView("alterarAssociado");
        mv.addObject(associado);
        mv.addObject("todosStatus", StatusAssociado.values());
        return mv;
    }

    @PostMapping(value = "/editar")
    public ModelAndView editar(@Valid Associado associado, BindingResult result, RedirectAttributes attributes, 
                                Model model) {
      
        if (result.hasErrors()) {
            return editarView(associado);
        }
        try {
            associadoService.editar(associado);
            attributes.addFlashAttribute("mensagemSucesso", "Associado "+ associado.getNome() + " alterado com sucesso.");
            return new ModelAndView("redirect:/associados/novo");
        } catch (NegocioException e) {
            model.addAttribute("mensagemErro", e.getMessage());
           System.out.println(e.getMessage());
           return new ModelAndView("redirect:/associados/novo");
       } 
    }

    @GetMapping(value="/{id}/delete")
	public ModelAndView excluir(@PathVariable Long id, RedirectAttributes attributes, Model model) {
        try{
            associadoService.remover(id);
            attributes.addFlashAttribute("mensagemSucesso", "Associado excluído com sucesso.");
		    return new ModelAndView ("redirect:/associados/listar");

        }catch (NegocioException e){
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            System.out.println(e.getMessage());
            return new ModelAndView ("redirect:/associados/listar");
        }
	}
    
    @RequestMapping("/detalharAssociado/{id}")
    public ModelAndView detalharAssociado(@PathVariable("id") Associado associado) {
        ModelAndView mv = new ModelAndView("alterarAssociado");
        mv.addObject(associado);
        mv.addObject("todosStatus", StatusAssociado.values());
        return mv;
    }

    @ModelAttribute("todosStatus")
    public List<StatusAssociado> getStatusAssociados(){
        return Arrays.asList(StatusAssociado.values());
    }

}
