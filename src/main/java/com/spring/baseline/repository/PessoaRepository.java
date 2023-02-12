package com.spring.baseline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.baseline.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
