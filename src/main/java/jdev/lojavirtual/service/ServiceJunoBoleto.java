package jdev.lojavirtual.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import jdev.lojavirtual.enums.ApiTokenIntegracao;
import jdev.lojavirtual.model.AccessTokenJunoAPI;
import jdev.lojavirtual.model.BoletoJuno;
import jdev.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.lojavirtual.model.dto.BoletoGeradoApiJunoDTO;
import jdev.lojavirtual.model.dto.CobrancaJunoApi;
import jdev.lojavirtual.model.dto.ConteudoBoletoJuno;
import jdev.lojavirtual.model.dto.ObjetoPostCarneJunoDTO;
import jdev.lojavirtual.repository.AccesTokenJunoRepository;
import jdev.lojavirtual.repository.BoletoJunoRepository;
import jdev.lojavirtual.repository.Vd_Cp_Loja_virt_repository;


@Service
public class ServiceJunoBoleto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AccessTokenJunoService accessTokenJunoService;
	
	@Autowired
	private AccesTokenJunoRepository accesTokenJunoRepository;
	
	@Autowired
	private Vd_Cp_Loja_virt_repository vd_Cp_Loja_virt_repository;
	
	@Autowired
	private BoletoJunoRepository boletoJunoRepository;
	
	public String gerarCarnerAPi(ObjetoPostCarneJunoDTO objetoPostCarneJunoDTO) throws Exception {
		
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.findById(objetoPostCarneJunoDTO.getIdVenda()).get();
		
		CobrancaJunoApi cobrancaJunoApi = new CobrancaJunoApi();
		cobrancaJunoApi.getCharge().setPixKey(ApiTokenIntegracao.CHAVE_BOLETO_PIX);
		cobrancaJunoApi.getCharge().setDescription(objetoPostCarneJunoDTO.getDescription());
		cobrancaJunoApi.getCharge().setAmount(Float.valueOf(objetoPostCarneJunoDTO.getTotalAmount()));
		cobrancaJunoApi.getCharge().setInstallments(Integer.parseInt(objetoPostCarneJunoDTO.getInstallments()));
		
		Calendar dataVencimento = Calendar.getInstance();
		dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
		cobrancaJunoApi.getCharge().setDueDate(dateFormat.format(dataVencimento.getTime()));
		
		cobrancaJunoApi.getCharge().setFine(BigDecimal.valueOf(1.00));
		cobrancaJunoApi.getCharge().setInterest(BigDecimal.valueOf(1.00));
		cobrancaJunoApi.getCharge().setMaxDueDate(10);
		cobrancaJunoApi.getCharge().getPaymentTypes().add("BOLETO_PIX");

		cobrancaJunoApi.getBilling().setName(objetoPostCarneJunoDTO.getPayerName());
		cobrancaJunoApi.getBilling().setDocument(objetoPostCarneJunoDTO.getPayerCpfCnpj());
		cobrancaJunoApi.getBilling().setEmail(objetoPostCarneJunoDTO.getEmail());
		cobrancaJunoApi.getBilling().setPhone(objetoPostCarneJunoDTO.getPayerPhone());

		AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		if (accessTokenJunoAPI != null) {
			
			Client client = new HostIgoringCliente("https://api.juno.com.br/").hostIgnoreClient();
			WebResource webResource = client.resource("https://api.juno.com.br/charges");

			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(cobrancaJunoApi);
			
			ClientResponse clientResponse = (ClientResponse) webResource
					.accept("application/json;charset=UTF-8")
					.header("Content-Type", "application/json")
					.header("X-API-Version", 2)
					.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
					.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
					.post(ClientResponseContext.class, "{ \"type\": \"RAMDOM-KEY\"}");
			
			String stringRetorno = clientResponse.getEntity(String.class);
			
			if (clientResponse.getStatus() == 200) {
				
				clientResponse.close();
				objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);// Converte relacionamento um para muitos dentro desse json
				
				BoletoGeradoApiJunoDTO jsonRetornoObject = objectMapper.readValue(stringRetorno, 
							new TypeReference<BoletoGeradoApiJunoDTO>() {});
				
				int recorrencia = 1;
				
				List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();
				
				for (ConteudoBoletoJuno c : jsonRetornoObject.get_embedded().getCharges()) {
					BoletoJuno boletoJuno = new BoletoJuno();
					boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
					boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
					boletoJuno.setCode(c.getCode());
					boletoJuno.setLink(c.getLink());
					boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate())));
					boletoJuno.setCheckouUrl(c.getCheckoutUrl());
					boletoJuno.setValor(new BigDecimal(c.getAmount()));
					boletoJuno.setIdChrBoleto(c.getId());
					boletoJuno.setInstallmentLink(c.getInstallmenteLink());
					boletoJuno.setIdPix(c.getPix().getId());
					boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
					boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
					boletoJuno.setRecorrencia(recorrencia);// se jerou mais de um boleto
					
					boletoJunos.add(boletoJuno);
					recorrencia ++;
					
				}
				
				boletoJunoRepository.saveAllAndFlush(boletoJunos);
				
				return boletoJunos.get(0).getLink();
						
			}else {
				return stringRetorno;
			}
			
		}else {
			return "Não existe chave de acesso para a API";
		}
		
	}
	
	public String geraChaveBoletoPix() throws Exception {
		
		
		AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
		Client client = new HostIgoringCliente("https://api.juno.com.br/").hostIgnoreClient();
		WebResource webResource = client.resource("https://api.juno.com.br/pix/keys");

		ClientResponse clientResponse = (ClientResponse) webResource
				.accept("application/json;charset=UTF-8")
				.header("Content-Type", "application/json")
				.header("X-API-Version", 2)
				.header("X-Resource-Token", ApiTokenIntegracao.TOKEN_PRIVATE_JUNO)
				.header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
				.post(ClientResponseContext.class, "{ \"type\": \"RAMDOM-KEY\"}");
				
//				.header("X-Idempotency-Key", "chave-boleto-pix")
		return clientResponse.getEntity(String.class);
	}
	
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
