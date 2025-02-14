package com.api.gestaoassociacao.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.api.gestaoassociacao.service.AssociadoService;
import com.api.gestaoassociacao.service.MensalidadeService;

@Controller
public class IndexController {

    
    @Autowired
    private AssociadoService associadoService;
    @Autowired
    private MensalidadeService mensalidadeService;

    @GetMapping("/home")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");
       
        mv.addObject("totalCadastrados", associadoService.getTotalAssociadosCadastrados());
        mv.addObject("sumMensalidadesEmAberto", mensalidadeService.getTotalMensalidadesEmAberto());
        mv.addObject("totalMensalidadesEmAtraso", mensalidadeService.getTotalMensalidadesEmAtraso());
        
        return mv;
    }

     @GetMapping("/")
    public String home(Model model, Principal principal) {
        model.addAttribute("userName", principal.getName());
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}