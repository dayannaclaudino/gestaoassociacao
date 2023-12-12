package com.api.gestaoassociacao.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/home")
    public String index(Principal principal) {
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}