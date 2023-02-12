package com.spring.baseline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.baseline.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/inicio")
public class InicioController {

	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping("/")
	public ModelAndView inicio(HttpSession session, @AuthenticationPrincipal User user) {
		if(session != null) {
			session.setAttribute("usuario", usuarioService.buscarPorEmail(user.getUsername()));
		}
		return new ModelAndView("/inicio/index");
	}
}
