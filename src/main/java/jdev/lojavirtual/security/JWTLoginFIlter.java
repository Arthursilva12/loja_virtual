package jdev.lojavirtual.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.lojavirtual.model.Usuario;

public class JWTLoginFIlter extends AbstractAuthenticationProcessingFilter{

	// Configura o gerenciador de autenticação
	public JWTLoginFIlter(String url,AuthenticationManager authenticationManager) {
		// Obriga a autenticar a url
		super(new AntPathRequestMatcher(url));
		
		// Gerenciador de autenticação
		setAuthenticationManager(authenticationManager);
		
	}
	
	@Override// Retorna o usuário processado na autenticação
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		
		// Retorna user com login e senha
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}

	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		try {
			new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
	
		if(failed instanceof BadCredentialsException) {
			response.getWriter().write("User e senha não encontrado");
		}else {
			response.getWriter().write("Falha ao loga: " + failed.getMessage());

		}
//		super.unsuccessfulAuthentication(request, response, failed);
	}
	
	
}
