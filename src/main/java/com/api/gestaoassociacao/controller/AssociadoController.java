package com.api.gestaoassociacao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.Exception.*;
import com.api.gestaoassociacao.repository.filter.AssociadoFilter;
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
 
        return mv;
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Associado associado, Errors errors, RedirectAttributes attributes) {
       
        if (errors.hasErrors()) {
            return novo(associado);
        }
        try {
            associadoService.salvar(associado);
            return new ModelAndView("redirect:/associados/novo");

        } catch (IllegalArgumentException e) {
            errors.rejectValue("dataNascimento", null, e.getMessage());
            attributes.addFlashAttribute("mensagem", "Associado salvo com sucesso.");
            return new ModelAndView("redirect:/associados/novo");

        }
    }


    @RequestMapping("{id}")
    public ModelAndView edicao(@PathVariable("id") Associado associado) {
        ModelAndView mv = new ModelAndView(VIEW);
        mv.addObject(associado);
        return mv;
    }

    @DeleteMapping(value = "{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            associadoService.remover(id);
        } catch (NegocioException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/associados/pesquisar";
        }

        attributes.addFlashAttribute("mensagem", "Associado excluído com sucesso.");
        return "redirect:/associados/pesquisar";
    }

    @RequestMapping("/pesquisar")
    public ModelAndView pesquisar(@ModelAttribute("filtro") AssociadoFilter filtro) {
        List<Associado> todosAssociados = associadoService.filtrar(filtro);

        ModelAndView mv = new ModelAndView("listaAssociados");
        mv.addObject("associados", todosAssociados);
        return mv;
    }

    /*
     * @GetMapping("/associados")
     * public List<Associado> buscarAssociados(){
     * return associadoService.buscarTodosAssociados();
     * 
     * }
     */
}
