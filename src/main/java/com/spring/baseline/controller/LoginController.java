package com.spring.baseline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String formularioLogin() {
		return "login/index";
	}
	
	@GetMapping("/password")
	public String formularioEsqueceuSenha() {
		return "login/edit-senha/index";
	}
}
