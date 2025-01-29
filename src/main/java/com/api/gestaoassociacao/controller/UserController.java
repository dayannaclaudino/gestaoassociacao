package com.api.gestaoassociacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.gestaoassociacao.model.User;
import com.api.gestaoassociacao.repository.UserRepository;
import com.api.gestaoassociacao.security.IUserService;

@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
	private IUserService userService;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/novo")
	public String viewCadastroUsuario(User user, Model model) {
	
		model.addAttribute("user", user);
		return "cadastroUsuario";
	}
	
	@PostMapping("/salvar")
	public ModelAndView saveUser(@ModelAttribute User user, Model model){
		ModelAndView mv = new ModelAndView("cadastroUsuario");
		try {
			userService.saveUser(user); 
			model.addAttribute("mensagemSucesso", "Usuário salvo com sucesso!");
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("mensagemErro", "O nome de usuário já está em uso. Por favor, escolha outro.");
		}
		return mv;
	}

	@GetMapping("/listar")
	public ModelAndView getUsers(){
		ModelAndView mv = new ModelAndView("listaUsuario");
		mv.addObject("users", userRepository.findAll());
		return mv;
	}

	@RequestMapping("/editar/{id}")
    public ModelAndView editarView(@PathVariable("id") User user) {
        ModelAndView mv = new ModelAndView("cadastroUsuario");
        mv.addObject(user);
        return mv;
    }

	@GetMapping("/{userId}/excluir")
	public ModelAndView excluir(@PathVariable("userId") Long userId, RedirectAttributes attributes) {
		
		userRepository.deleteById(userId);
		ModelAndView mv = new ModelAndView("listaUsuario");
		mv.addObject("users", userRepository.findAll());
		attributes.addFlashAttribute("mensagemSucesso",  "Usuário excluído com sucesso!");
		return mv;
	}


}

