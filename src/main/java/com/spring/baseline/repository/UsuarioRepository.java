package com.spring.baseline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.spring.baseline.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.email like :email")
	Usuario findByEmail(String email);

	@Query("select u from Usuario u where u.email like :email AND u.ativo = true")
	Usuario findByEmailAndAtivo(String email);
}
