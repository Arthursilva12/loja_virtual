package jdev.lojavirtual;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.lojavirtual.enums.ApiTokenIntegracao;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TesteAPIMelhorEnvio {

	public static void main(String[] args) throws IOException {	
		/*
		// Insere etiquetas de frete
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\n \"service\": 3,\n \"agency\": 49,\n \"from\": {\n \"name\": \"Arthur\",\n \"phone\": \"53984470102\",\n \"email\": \"contato@melhorenvio.com.br\",\n \"document\": \"16571478358\",\n \"company_document\": \"89794131000100\",\n \"state_register\": \"123456\",\n \"address\": \"rua 17 quadra 78 lote 67 parque estrela dalva 18\",\n \"complement\": \"Complemento\",\n \"number\": \"1\",\n \"district\": \"Bairro\",\n \"city\": \"São Paulo\",\n \"country_id\": \"BR\",\n \"postal_code\": \"01002001\",\n  \"note\": \"observação\"\n },\n \"to\": {\n \"name\": \"Guilherme\",\n \"phone\": \"53984470102\",\n \"email\": \"contato@melhorenvio.com.br\",\n \"document\": \"25404918047\",\n \"company_document\": \"07595604000177\",\n \"state_register\": \"123456\",\n \"address\": \"rua 19 quadra 79 lote 97 parque estrela dalva 98\",\n \"complement\": \"Complemento\",\n \"number\": \"2\",\n \"district\": \"Bairro\",\n \"city\": \"Porto Alegre\",\n  \"state_abbr\": \"RS\",\n \"country_id\": \"BR\",\n \"postal_code\": \"90570020\",\n  \"note\": \"observação\"\n },\n  \"products\": [\n  {\n  \"name\": \"Papel adesivo para etiquetas 1\",\n  \"quantity\": 3,\n \"unitary_value\": 100.00\n },\n {\n \"name\": \"Papel adesivo para etiquetas 2\",\n \"quantity\": 1,\n \"unitary_value\": 100.00\n }\n    ],\n \"volumes\": [\n {\n \"height\": 15,\n \"width\": 20,\n \"length\": 10,\n \"weight\": 3.5\n }\n    ],\n    \"options\": {\n \"insurance_value\": 10.00,\n \"receipt\": false,\n \"own_hand\": false,\n \"reverse\": false,\n \"non_commercial\": false,\n \"invoice\": {\n \"key\": \"31190307586261000184550010000092481404848162\"\n},\n \"platform\": \"Nome da Plataforma\",\n \"tags\": [\n {\n \"tag\": \"Identificação do pedido na plataforma, exemplo: 1000007\",\n \"url\": \"Link direto para o pedido na plataforma, se possível, caso contrário pode ser passado o valor null\"\n }\n ]\n }\n}");
		Request request = new Request.Builder()
		  .url( ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/cart")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
		System.out.println(jsonNode);
		*/
		/*
		// Faz a compra do frete para a etiqueta
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9fae8b0d-902f-4276-8e74-9a5355d6c388\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/checkout")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
		*/
		/*
		// Gera as etiquetas
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9fae8b0d-902f-4276-8e74-9a5355d6c388\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/generate")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
		*/
		
		// Faz impresão das etiquestas
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9fae8b0d-902f-4276-8e74-9a5355d6c388\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/print")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
	}
	
}
