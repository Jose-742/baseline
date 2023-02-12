package com.spring.baseline.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.spring.baseline.model.Perfil;
import com.spring.baseline.model.Pessoa;
import com.spring.baseline.model.Usuario;
import com.spring.baseline.service.PerfilService;
import com.spring.baseline.service.PessoaService;
import com.spring.baseline.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {
	
	@Autowired
	private PerfilService servicePerfil;
	
	@Autowired
	private UsuarioService serviceUsuario;
	
	@Autowired
	private PessoaService servicePessoa;
	
	@GetMapping("/cadastro")
	public String formularioCadastro(Pessoa pessoa) {
		return "login/edit/index";
	}
	
	// pagina de resposta do cadastro de paciente
	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {		
		return "fragments/mensagem";
	}
	@PostMapping("/save")
	public String save(@Valid Pessoa pessoa, BindingResult result, HttpServletRequest request) {
		if(result.hasErrors()) {
			return"login/edit/index";
		}
		String senha = pessoa.getUsuario().getSenha();
		
		List<Perfil> perfis = new ArrayList<>();
		perfis.add(servicePerfil.perfil(2L));//User
		Usuario usuario = pessoa.getUsuario();
		usuario.setNome(pessoa.getNome());
		usuario.setPerfil(perfis);
		serviceUsuario.save(usuario);
		servicePessoa.save(pessoa);
		
		try {
			//ao se cadastrar o usuario é autenticado automaticamente no sistema
			//request.login nao aceita senha criptografada para se autenticar 
			request.login(pessoa.getUsuario().getEmail(), senha);
		} catch (ServletException e) {
			return "redirect:/login?error";
		}
		
		return "redirect:/pessoa/cadastro/realizado";
	}

}
