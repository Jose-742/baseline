package com.spring.baseline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.baseline.model.Perfil;
import com.spring.baseline.repository.PerfilRepository;

@Service
public class PerfilService {

	@Autowired
	private PerfilRepository repository;
	
	
	public Perfil perfil(Long id) {
		return repository.findById(id).get();
	}
}
