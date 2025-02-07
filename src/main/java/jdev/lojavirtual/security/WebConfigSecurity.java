package jdev.lojavirtual.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSessionListener;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity implements HttpSessionListener{

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET,"/salvarAcesso")
				 				.permitAll().requestMatchers(HttpMethod.POST,"/salvarAcesso"));
	        
		 return http.build();
	}
	
	
	public void configureSecurity(HttpSecurity http) throws Exception {
        this.securityFilterChain(http);
    }
	
//	public void configureGet(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/salvarAcesso").permitAll()
//				.requestMatchers(HttpMethod.POST, "/salvarAcesso"));
//	}
	
//	public void configureGet(WebSecurity web) throws Exception {
//		web.ignoring().requestMatchers(HttpMethod.GET, "/acessoSalvar")
//					.requestMatchers(HttpMethod.POST, "/acessoSalvar");
//	}
	
}
