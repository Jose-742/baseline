package com.spring.baseline.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.baseline.service.UsuarioService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
		//.httBasic autoriza que essa url possa fazer requisiçoes rest via javaScript sem ela somente se o usuario estiver autenticado
		//.httpBasic().and().authorizeHttpRequests().requestMatchers("img/**").permitAll().and()
		.authorizeHttpRequests((authorize)->{
			try {
				authorize
				.requestMatchers("/style/**","/dist/**", "/plugins/**", "/script/**","/img/**").permitAll()
				.requestMatchers("/password/**").permitAll()
				.requestMatchers("/pessoa/cadastro/**", "/pessoa/save/**").permitAll()
					.anyRequest()
					.authenticated()
					.and()
					.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/", true)//sempre que fizer login ele redireciona para /
					.permitAll()
					.and()
	        		.logout().deleteCookies("JSESSIONID").permitAll()
	        		 .and()
                     .sessionManagement()
                         .maximumSessions(1)//maximo de sessao 1
                         .expiredUrl("/login");//sessao anterior e redirecionada para pagiana de login
			} catch (Exception e) {
				e.printStackTrace();
			}		
		})
		.formLogin(Customizer.withDefaults());
		return http.build();
	}
	

	@Bean
	AuthenticationManager authManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UsuarioService service) 
	  throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)  	    		
	      .userDetailsService(service)
	      .passwordEncoder(bCryptPasswordEncoder)
	      .and()
	      .build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}
