package jdev.lojavirtual.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdev.lojavirtual.ApplicationContextLoad;
import jdev.lojavirtual.model.Usuario;
import jdev.lojavirtual.repository.UsuarioRepository;


// Criar autenticaçao e retonorna também a autenticação JWT
// Gera o token de authentication, recupera e depois valida
@Service
@Component
public class JWTTokenAutenticacaoService {
	// Toke de validade de 11 dias
	private static final long EXPIRATION_TIME =	950400000;
	// Chave de senha para junta com o JWT
	private static final String SECRET = "dfsgsafgdasjb_++lksndf";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	// Gera o token e da resposta para o cliente o com JWT
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		// Montagem do token
		String JWT = Jwts.builder()// Chama o gerador de token
				.setSubject(username)// Adiciona o user
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// Tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();// Chave de compactação
		
		// Ex: Bearer *- /sdasdsdsadasdasdasdasdasdas.sdasdsdsadasdasdasdasdasdas;sdasdsdsadasdasdasdasdasdas*
		String token = TOKEN_PREFIX + " " + JWT;
		// Dá resposta pra tela e para o cliente
		response.addHeader(HEADER_STRING, token);
		
		liberacaoCors(response);
		// Usado para ver no postman para teste
		response.getWriter().write("{\"Authorization\": \""+ token +"\"}");
 	}
	
	// Retorna o usuário com token ou caso não seja valido retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(HEADER_STRING);

		if (token != null) {
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			// Faz a validação do token do usuário na requisição e obter o USER
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenLimpo).getBody().getSubject();

			if (user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
							.getBean(UsuarioRepository.class).findUserByLogin(user);

				if (usuario != null) {// faz authenticaiton
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
				}
			}
		}

		liberacaoCors(response);
		return null;
	}
	
	// fazendo liberação contra erros de Cors no navegador
	private void liberacaoCors(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}

		
	}
	
}
