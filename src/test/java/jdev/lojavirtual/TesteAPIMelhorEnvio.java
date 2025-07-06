package jdev.lojavirtual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.lojavirtual.enums.ApiTokenIntegracao;
import jdev.lojavirtual.model.dto.EmpresaTransporteDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TesteAPIMelhorEnvio {

	public static void main(String[] args) throws IOException {
		
		OkHttpClient client = new OkHttpClient();// Instancia objeto da requisição
		MediaType mediaType = MediaType.parse("application/json");// Tipo dos dados em JSON
		RequestBody body = RequestBody.create(mediaType, "{ \"from\": { \"postal_code\": \"01002001\" }, \"to\": { \"postal_code\": \"90570020\" }, \"package\": { \"height\": 4, \"width\": 12, \"length\": 17, \"weight\": 0.3 }, \"options\": { \"insurance_value\": 1180.87, \"receipt\": false, \"own_hand\": false }, \"services\": \"1,2,3,4,7,11\" }");
		// instancia objeto de requisição
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/calculate")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
//		System.out.println(response.body().string());
		
		JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
		
		java.util.Iterator<JsonNode> iterator = jsonNode.iterator();
		
		List<EmpresaTransporteDTO> empresaTransporteDTOs = new ArrayList<EmpresaTransporteDTO>();
		
		while(iterator.hasNext()) {
			JsonNode node = iterator.next();
			
			EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();
			
			if (node.get("id") != null) {
				empresaTransporteDTO.setId(node.get("id").asText());
			}
			
			if (node.get("name") != null) {
				empresaTransporteDTO.setNome(node.get("name").asText());
			}
			
			if (node.get("price") != null) {
				empresaTransporteDTO.setValor(node.get("price").asText());
			}
			
			if (node.get("company") != null) {
				empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
				empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
			}
			
			if (empresaTransporteDTO.dadosOK()) {
				empresaTransporteDTOs.add(empresaTransporteDTO);
			}
		}
		
		System.out.println(empresaTransporteDTOs);
		
		
	}
	
}
