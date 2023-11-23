package com.api.gestaoassociacao.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.model.Associado;
import com.api.gestaoassociacao.repository.AssociadoRepository;
import com.api.gestaoassociacao.repository.DependenteRepository;
import com.api.gestaoassociacao.repository.filter.AssociadoFilter;
import com.api.gestaoassociacao.service.AssociadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/associados")
public class AssociadoController {

    private static final String VIEW = "cadastroAssociado";

    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private AssociadoRepository associadoRepository;
    @Autowired
    private DependenteRepository dependenteRepository;

    @RequestMapping("/novo")
    public ModelAndView novo(Associado associado) {
        ModelAndView mv = new ModelAndView(VIEW);
 
        return mv;
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ModelAndView salvar(@Valid Associado associado, BindingResult result, RedirectAttributes attributes) {
       
        if (result.hasErrors()) {
            return novo(associado);
        }
        try {
            associadoService.salvar(associado);
            attributes.addFlashAttribute("success", "Associado salvo com sucesso.");
            return new ModelAndView("redirect:/associados/novo");
            

        } catch (IllegalArgumentException e) {
            result.rejectValue("dataNascimento", null, e.getMessage());
            attributes.addFlashAttribute("error", "Erro Data de nascimento ou formato errado.");
            return new ModelAndView("redirect:/associados/novo");

        }
    }

    @RequestMapping("/listar")
    public ModelAndView listar(@ModelAttribute("filtro") AssociadoFilter filtro, @PageableDefault(size = 6) Pageable pageable) {

        ModelAndView mv = new ModelAndView("listaAssociados");

        Page<Associado> page = associadoService.findAllPageable(pageable);
        List<Associado> todosAssociados = associadoService.filtrar(filtro);
  
        mv.addObject("page", page);
        mv.addObject("associados", todosAssociados);
        return mv;
    }


    @RequestMapping("{id}")
    public ModelAndView edicao(@PathVariable("id") Associado associado) {
        ModelAndView mv = new ModelAndView(VIEW);
        mv.addObject(associado);
        return mv;
    }

    @GetMapping(value="/{id}/delete")
	public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        try{
            associadoService.remover(id);
		return "redirect:/associados/listar";
        }catch (EmptyResultDataAccessException e){
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/associados/listar";
        }
	}

    @RequestMapping("/details/{codigo}")
    public ModelAndView dependenteAssociado(@PathVariable("codigo") Long id) {
        
         Optional<Associado> associado = associadoRepository.findById(id);

        ModelAndView mv = new ModelAndView("cadastroDependentes");
        mv.addObject("associado", associado.get());
        mv.addObject("dependentes", dependenteRepository.getDependentes(id));
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
