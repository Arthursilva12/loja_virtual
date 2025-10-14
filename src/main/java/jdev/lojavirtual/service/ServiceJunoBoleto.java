package jdev.lojavirtual.service;

import java.io.Serializable;

import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import jdev.lojavirtual.model.AccessTokenJunoAPI;
import jdev.lojavirtual.repository.AccesTokenJunoRepository;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;

@Service
public class ServiceJunoBoleto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AccessTokenJunoService accessTokenJunoService;
	
	@Autowired
	private AccesTokenJunoRepository accesTokenJunoRepository;
	
	public AccessTokenJunoAPI obterTokenApiJuno() throws Exception {
		
		AccessTokenJunoAPI accessTokenJunoAPI = accessTokenJunoService.buscaTokenAtivo();
		
		if (accessTokenJunoAPI == null || (accessTokenJunoService != null && accessTokenJunoAPI.expirado())) {
		
			String clientID = "";
			String clientSecret = "";
			
			Client client = new HostIgoringCliente("https://api.juno.com.br/").hostIgnoreClient();
			
			WebResource webResource = client.resource("https://api.juno.com.br/authorization-server/oauth/token?grant_type=client_credentials");
			
			String basicChave = clientID + ":" + clientSecret;
			String token_autenticacao = DatatypeConverter.printBase64Binary(basicChave.getBytes());
			
			ClientResponse clientResponse = webResource
					.accept(MediaType.APPLICATION_FORM_URLENCODED)
					.type(MediaType.APPLICATION_FORM_URLENCODED)
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Authorization", "Basic " + token_autenticacao)
					.post(ClientResponse.class);
			
			if (clientResponse.getStatus() == 200) {// sucesso
				accesTokenJunoRepository.deleteAll();
				accesTokenJunoRepository.flush();
				
				AccessTokenJunoAPI accessTokenJunoAPI2 = clientResponse.getEntity(AccessTokenJunoAPI.class);
				accessTokenJunoAPI2.setToken_acesso(token_autenticacao);
				
				accessTokenJunoAPI2 = accesTokenJunoRepository.saveAndFlush(accessTokenJunoAPI2);
				return accessTokenJunoAPI2;
			}else {
				return null;
			}
			
		}else {
			return accessTokenJunoAPI;
		}
		
	}
	
}
