package jdev.lojavirtual.security;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jdev.lojavirtual.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements HttpSessionListener {

	@Autowired
	private ImplementacaoUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.disable().authorizeRequests().antMatchers("/").permitAll()
			.antMatchers("/index").permitAll().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")// redireciona ou da um retorno para index quando desloga 
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))// mapeia o logout do sistema
			.and().addFilterAfter(new JWTLoginFIlter("/login", authenticationManager()),
										UsernamePasswordAuthenticationFilter.class)// filtra as requisições para o login de JWT
		
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override// ira consultar o user no banco com o spring security
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	 
	
	@Override// Ignora algumas ULR livres de autenticação
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers(HttpMethod.GET, "/salvarAcesso", "/deleteAcesso")
//					.antMatchers(HttpMethod.POST, "/salvarAcesso", "/deleteAcesso");
	}
}
