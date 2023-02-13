package com.spring.baseline.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.spring.baseline.model.Usuario;
import com.spring.baseline.repository.UsuarioRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private EmailService emailService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailAtivo(email);
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		return new User(usuario.getEmail(), usuario.getSenha(), true, true, true, true, usuario.getAuthorities());
	}

	public Usuario cryptSenha(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		return usuario;
	}

	public Usuario save(Usuario usuario) {
		return repository.save(cryptSenha(usuario));
	}

	public Usuario update(Usuario usuario) {
		return repository.save(cryptSenha(usuario));
	}

	public Usuario buscarPorEmailAtivo(String email) {
		return repository.findByEmailAndAtivo(email);
	}

	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);
	}

	public void enviarEmail(Usuario usuario) throws MessagingException {
		emailDeConfirmacaoDeCadastro(usuario.getEmail());
	}

	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException {
		String codigo = Base64Utils.encodeToString(email.getBytes());
		emailService.enviarPedidoDeConfirmacaoDeCadastro(email, codigo);
	}

	public boolean ativarCadastroPaciente(String codigo) {
		String email = new String(Base64Utils.decodeFromString(codigo));
		Usuario usuario = buscarPorEmail(email);
		if (usuario.getId() == null) {
			return false;
		}
		usuario.setAtivo(true);
		return true;
	}

	public boolean pedidoRedefinicaoDeSenha(String email) throws MessagingException {
		Usuario usuario = buscarPorEmailAtivo(email);
		if(usuario == null) {
			return false;
		}
		String verificador = RandomStringUtils.randomAlphabetic(6);
		usuario.setCodigoVerificador(verificador);//savar codigo verificador
		
		emailService.enviarPedidoRedefinicaoSenha(email, verificador);
		return true;
	}

	public void auterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(senha);
		repository.save(cryptSenha(usuario));
	}
}
