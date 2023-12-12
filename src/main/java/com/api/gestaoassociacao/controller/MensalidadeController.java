package com.api.gestaoassociacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.Exception.NegocioException;
import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.model.Mensalidade;
import com.api.gestaoassociacao.model.enums.SituacaoMensalidade;
import com.api.gestaoassociacao.model.enums.StatusAssociado;
import com.api.gestaoassociacao.model.enums.Tipo;
import com.api.gestaoassociacao.service.MensalidadeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/mensalidades")
public class MensalidadeController {
    
    private static final String VIEW = "CadastroMensalidade";

    @Autowired
    private MensalidadeService mensalidadeService;


    @RequestMapping("/novo")
    public ModelAndView novo(Mensalidade mensalidade) {
        ModelAndView mv = new ModelAndView(VIEW);

        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasAsSituacoes", SituacaoMensalidade.values());
        return mv;
    }

    @PostMapping(value = "/salvar")
    public ModelAndView salvar(@Valid Mensalidade mensalidade, BindingResult result, RedirectAttributes attributes) {
      
        if (result.hasErrors()) {
            return novo(mensalidade);
        }
        try {
            mensalidadeService.salvar(mensalidade);
            attributes.addFlashAttribute("mensagemSucesso", "Associado salva com sucesso.");
            return new ModelAndView("redirect:/mensalidades/novo");
            
        } catch (NegocioException e) {
             attributes.addAttribute("mensagemErro", e.getMessage());
            System.out.println(e.getMessage());
            return novo(mensalidade);
        }    
    }

     @RequestMapping("{id}")
    public ModelAndView editar(@PathVariable("id") Mensalidade mensalidade) {
        ModelAndView mv = new ModelAndView(VIEW);
        mv.addObject(mensalidade);
        mv.addObject("todosTipos", Tipo.values());
        mv.addObject("todasAsSituacoes", SituacaoMensalidade.values());
        return mv;
    }

    @GetMapping(value="/{id}/delete")
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        try{
            mensalidadeService.remover(id);
		return "redirect:/mensalidades/listar";
        }catch (EmptyResultDataAccessException e){
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/mensalidades/listar";
        }
	}

    @RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long id) {
		return mensalidadeService.receber(id);
	}
}
