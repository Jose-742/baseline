package com.spring.baseline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.baseline.model.Usuario;
import com.spring.baseline.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = repository.findByEmail(email);
		if(usuario == null) {
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
	
	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);
	}
}
