package com.spring.baseline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.baseline.model.Pessoa;
import com.spring.baseline.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository repository;
	
	
	public Pessoa save(Pessoa pessoa) {
		return repository.save(pessoa);
	}
}
