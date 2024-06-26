package com.api.gestaoassociacao.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        return mv;
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}