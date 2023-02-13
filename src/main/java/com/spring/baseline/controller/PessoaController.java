package com.spring.baseline.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.baseline.model.Perfil;
import com.spring.baseline.model.Pessoa;
import com.spring.baseline.model.Usuario;
import com.spring.baseline.service.PerfilService;
import com.spring.baseline.service.PessoaService;
import com.spring.baseline.service.UsuarioService;

import jakarta.mail.MessagingException;
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

	// pagina de resposta do cadastro usuario
	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {
		return "fragments/mensagem";
	}

	// receber a requisicao de confirmacao de cadastro
	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr) {
		if (serviceUsuario.ativarCadastroPaciente(codigo)) {
			attr.addFlashAttribute("alerta", "sucesso");
			attr.addFlashAttribute("titulo", "Cadastro Ativado!");
			attr.addFlashAttribute("texto", "Parabéns, seu cadastro está ativo.");
			attr.addFlashAttribute("subtexto", "Siga com seu login/senha");
		} else {
			attr.addFlashAttribute("alerta", "erro");
			attr.addFlashAttribute("titulo", "Cadastro não Ativado!");
			attr.addFlashAttribute("texto", "Não foi possível ativar seu cadastro.");
			attr.addFlashAttribute("subtexto", "Entre em contato com o suporte.");
		}
		return "redirect:/login";
	}

	@PostMapping("/save")
	public String save(@Valid Pessoa pessoa, BindingResult result, HttpServletRequest request)
			throws MessagingException {

		if (result.hasErrors()) {
			return "login/edit/index";
		}
		if (serviceUsuario.buscarPorEmail(pessoa.getUsuario().getEmail()) != null) {
			result.addError(new ObjectError("fail", "Ops... Este e-mail já existe na base de dados."));
			return "login/edit/index";
		}

		List<Perfil> perfis = new ArrayList<>();
		perfis.add(servicePerfil.perfil(2L));// User
		Usuario usuario = pessoa.getUsuario();
		usuario.setNome(pessoa.getNome());
		usuario.setPerfil(perfis);
		serviceUsuario.save(usuario);
		servicePessoa.save(pessoa);
		serviceUsuario.enviarEmail(usuario);

		return "redirect:/pessoa/cadastro/realizado";
	}
	
	@GetMapping("/recuperar/senha")
	public String redefinirSenha(String email, ModelMap model) throws MessagingException {
		if(serviceUsuario.pedidoRedefinicaoDeSenha(email)) {
			model.addAttribute("sucesso", "Em instantes você receberá um e-mail para "
					+ "prosseguir com a redefinição de sua senha.");
			model.addAttribute("usuario", new Usuario(email));
			return "/login/edit-senha/recuperar-senha";
		}	
		model.addAttribute("fail","E-mail inválido");
		return "/login/edit-senha/index";
	}
	
	@PostMapping("/nova/senha/")
	public String confirmacaoDeRedefinicaoDeSenha(@Valid Usuario usuario, BindingResult result,ModelMap model) {
		Usuario user = serviceUsuario.buscarPorEmail(usuario.getEmail());
		if(result.hasErrors()) {
			return "/login/edit-senha/recuperar-senha";
		}
		if(!user.getCodigoVerificador().equals(usuario.getCodigoVerificador())) {
			result.addError(new ObjectError("fail","Código verificador não confere."));
			return "/login/edit-senha/recuperar-senha";
		}
		user.setCodigoVerificador(null);
		serviceUsuario.auterarSenha(user, usuario.getSenha());
		model.addAttribute("alerta", "sucesso");
		model.addAttribute("titulo", "Senha redefinida!");
		model.addAttribute("texto", "Você já pode logar no sistema.");
		return "/login/index";
	}

}
