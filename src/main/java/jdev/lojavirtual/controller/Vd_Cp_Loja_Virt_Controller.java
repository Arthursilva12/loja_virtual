package jdev.lojavirtual.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.lojavirtual.ExceptionLojaVirtualJava;
import jdev.lojavirtual.enums.ApiTokenIntegracao;
import jdev.lojavirtual.enums.StatusContaReceber;
import jdev.lojavirtual.model.ContaReceber;
import jdev.lojavirtual.model.Endereco;
import jdev.lojavirtual.model.ItemVendaLoja;
import jdev.lojavirtual.model.PessoaFisica;
import jdev.lojavirtual.model.StatusRastreio;
import jdev.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.lojavirtual.model.dto.ConsultaFreteDTO;
import jdev.lojavirtual.model.dto.EmpresaTransporteDTO;
import jdev.lojavirtual.model.dto.EnvioEtiquetaDTO;
import jdev.lojavirtual.model.dto.ItemVendaDTO;
import jdev.lojavirtual.model.dto.ProductsEnvioEtiquetaDTO;
import jdev.lojavirtual.model.dto.TagsEnvioDTO;
import jdev.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import jdev.lojavirtual.model.dto.VolumesEnvioEtiquetaDTO;
import jdev.lojavirtual.repository.ContaReceberRepository;
import jdev.lojavirtual.repository.EnderecoRepository;
import jdev.lojavirtual.repository.NotaFiscalVendaRepository;
import jdev.lojavirtual.repository.StatusRastreioRepository;
import jdev.lojavirtual.repository.Vd_Cp_Loja_virt_repository;
import jdev.lojavirtual.service.ServiceSendEmail;
import jdev.lojavirtual.service.VendaService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
public class Vd_Cp_Loja_Virt_Controller {

	@Autowired
	private Vd_Cp_Loja_virt_repository vd_Cp_Loja_virt_repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ContaReceberRepository contaReceberRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@ResponseBody
	@PostMapping(value = "/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtualJava, UnsupportedEncodingException, MessagingException {
		
		
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);
		 
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}
		
		// salva primeiro a venda e todos os dados
		vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.saveAndFlush(vendaCompraLojaVirtual);
		
		StatusRastreio sRastreio = new StatusRastreio();
		sRastreio.setCentroDistribuicao("LOja Local");
		sRastreio.setCidade("local");
		sRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		sRastreio.setEstado("local");
		sRastreio.setStatus("Inicio Compra");
		sRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		statusRastreioRepository.save(sRastreio);
		
		// associa a venda gravada no banco com a nota fiscal
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		// persiste  a nota fiscal novamente pra ficar amarrada na venda 
	 	notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
		
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		
		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		
		compraLojaVirtualDTO.setValorDesc(vendaCompraLojaVirtual.getValorDesconto()); 
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		
		
		for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setProduto(item.getProduto());
			itemVendaDTO.setQuantidade(item.getQuantidade());
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}
		
		ContaReceber contaReceber = new ContaReceber();
		contaReceber.setDescricao("venda da loja virtual n: " + vendaCompraLojaVirtual.getId());
		contaReceber.setDtPagamento(Calendar.getInstance().getTime());
		contaReceber.setDtVencimento(Calendar.getInstance().getTime());
		contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		contaReceber.setPessoa(vendaCompraLojaVirtual.getPessoa());
		contaReceber.setStatusContaReceber(StatusContaReceber.QUITADA);
		contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		
		contaReceberRepository.saveAndFlush(contaReceber);
		// Email para o comprador
		StringBuilder msgEmail = new StringBuilder();
		msgEmail.append("Olá,").append(pessoaFisica.getNome()).append("</br>");
		msgEmail.append("Você realizou a compra n: ").append(vendaCompraLojaVirtual.getId()).append("</br>");
		msgEmail.append("Na loja ").append(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());
		//Assunto, msg, destino
		serviceSendEmail.enviarEmailHtml("Compra Realizada!", msgEmail.toString(), pessoaFisica.getEmail());
		// Email para o vendedor
		msgEmail = new StringBuilder();
		msgEmail.append("Você realizou uma venda, n: ").append(vendaCompraLojaVirtual.getId());
		serviceSendEmail.enviarEmailHtml("Venda Realizada!", msgEmail.toString(), vendaCompraLojaVirtual.getEmpresa().getEmail());
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO,HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable("id") Long idVenda ) {
		
		VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_Loja_virt_repository.findByIdExclusao(idVenda);
		
		if(compraLojaVirtual == null) {
			compraLojaVirtual = new VendaCompraLojaVirtual();
		}
		
		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());
		
		compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
		
		compraLojaVirtualDTO.setValorDesc(compraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(compraLojaVirtual.getId()); 
		
		for (ItemVendaLoja item : compraLojaVirtual.getItemVendaLojas()) {
			
			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProduto());
			
			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/imprimeCompraEtiquetaFrete/{idVenda}")
	public ResponseEntity<String> imprimeCompraEtiquetaFrete(@PathVariable Long idVenda) throws ExceptionLojaVirtualJava, IOException {
		
		VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_Loja_virt_repository.findById(idVenda).orElseGet(null);
		
		if(compraLojaVirtual == null) {
			new ResponseEntity<String>("Venda não encontrada", HttpStatus.OK);
		} 
		
		List<Endereco> enderecos = enderecoRepository.enderecoPj(compraLojaVirtual.getEmpresa().getId());
		compraLojaVirtual.getEmpresa().setEnderecos(enderecos);
		
		EnvioEtiquetaDTO envioEtiquetaDTO = new EnvioEtiquetaDTO();
		envioEtiquetaDTO.setService(compraLojaVirtual.getServiceTransportadora());
		envioEtiquetaDTO.setAgency("49");
		envioEtiquetaDTO.getFrom().setName(compraLojaVirtual.getEmpresa().getNome());
		envioEtiquetaDTO.getFrom().setPhone(compraLojaVirtual.getEmpresa().getTelefone());
		envioEtiquetaDTO.getFrom().setEmail(compraLojaVirtual.getEmpresa().getEmail());
		envioEtiquetaDTO.getFrom().setCompany_document(compraLojaVirtual.getEmpresa().getCnpj());
		envioEtiquetaDTO.getFrom().setState_register(compraLojaVirtual.getEmpresa().getInscEstadual());
		envioEtiquetaDTO.getFrom().setAddress(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getRua());
		envioEtiquetaDTO.getFrom().setComplement(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
		envioEtiquetaDTO.getFrom().setNumber(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
		envioEtiquetaDTO.getFrom().setDistrict(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getEstado());
		envioEtiquetaDTO.getFrom().setCity(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
		envioEtiquetaDTO.getFrom().setCountry_id(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());
		envioEtiquetaDTO.getFrom().setPostal_code(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
		envioEtiquetaDTO.getFrom().setNote("Não há");
		
		
		envioEtiquetaDTO.getTo().setName(compraLojaVirtual.getPessoa().getNome());
		envioEtiquetaDTO.getTo().setPhone(compraLojaVirtual.getPessoa().getTelefone());
		envioEtiquetaDTO.getTo().setEmail(compraLojaVirtual.getPessoa().getEmail());
		envioEtiquetaDTO.getTo().setDocument(compraLojaVirtual.getPessoa().getCpf());
		envioEtiquetaDTO.getTo().setAddress(compraLojaVirtual.getPessoa().enderecoEntrega().getRua());
		envioEtiquetaDTO.getTo().setComplement(compraLojaVirtual.getPessoa().enderecoEntrega().getComplemento());
		envioEtiquetaDTO.getTo().setNumber(compraLojaVirtual.getPessoa().enderecoEntrega().getNumero());
		envioEtiquetaDTO.getTo().setDistrict(compraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
		envioEtiquetaDTO.getTo().setCity(compraLojaVirtual.getPessoa().enderecoEntrega().getCidade());
		envioEtiquetaDTO.getTo().setState_abbr(compraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
		envioEtiquetaDTO.getTo().setCountry_id(compraLojaVirtual.getPessoa().enderecoEntrega().getUf());
		envioEtiquetaDTO.getTo().setPostal_code(compraLojaVirtual.getPessoa().enderecoEntrega().getCep());
		envioEtiquetaDTO.getTo().setNote("Não há");
		
		List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();
		
		for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {
			
			ProductsEnvioEtiquetaDTO dto = new ProductsEnvioEtiquetaDTO();
			dto.setName(itemVendaLoja.getProduto().getNome());
			dto.setQuantity(itemVendaLoja.getQuantidade().toString());
			dto.setUnitary_value("" + itemVendaLoja.getProduto().getValorVenda().doubleValue());
			
			products.add(dto);
		}
		
		envioEtiquetaDTO.setProducts(products);
		
		List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();
		
		for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {
			VolumesEnvioEtiquetaDTO dto = new VolumesEnvioEtiquetaDTO();
			dto.setHeight(itemVendaLoja.getProduto().getAltura().toString());
			dto.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
			dto.setWeight(itemVendaLoja.getProduto().getPeso().toString());
			dto.setWidth(itemVendaLoja.getProduto().getLargura().toString());
			
			volumes.add(dto);
		}
		
		envioEtiquetaDTO.setVolumes(volumes);
		
		envioEtiquetaDTO.getOptions().setInsurance_value("" + compraLojaVirtual.getValorTotal().doubleValue());
		envioEtiquetaDTO.getOptions().setReceipt(false);
		envioEtiquetaDTO.getOptions().setReverse(false);
		envioEtiquetaDTO.getOptions().setNon_commercial(false);
		envioEtiquetaDTO.getOptions().getInvoice().setKey(compraLojaVirtual.getNotaFiscalVenda().getNumero());
		envioEtiquetaDTO.getOptions().setPlatform(compraLojaVirtual.getEmpresa().getNomeFantasia());
		
		TagsEnvioDTO dtoTagEnvio = new TagsEnvioDTO();
		dtoTagEnvio.setTag("Identificação do pedido na plataforma, exemplo:" + compraLojaVirtual.getId());
		dtoTagEnvio.setUrl(null);
		
		envioEtiquetaDTO.getOptions().getTags().add(dtoTagEnvio);
		
		String jsonEnvio = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);
		
		OkHttpClient client = new OkHttpClient();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvio);
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url( ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/cart")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		String respostaJson = response.body().string();
		
		if (respostaJson.contains("error")) {
			throw new ExceptionLojaVirtualJava(respostaJson);
		}
		
		JsonNode jsonNode = new ObjectMapper().readTree(respostaJson);
		java.util.Iterator<JsonNode> iterator = jsonNode.iterator();
		
		String idEtiqueta = "";
		while(iterator.hasNext()) {
			JsonNode node = iterator.next(); 
			
			if (node.get("id") != null) {
				idEtiqueta = node.get("id").asText();
			}else {
				idEtiqueta = node.asText();
			}
			break;
		}
		
		if (idEtiqueta != null && !idEtiqueta.isEmpty() && compraLojaVirtual.getId() != null) {
			// Salvando o codigo da etiquta
			jdbcTemplate.execute("update vd_cp_loja_virt set codigo_etiqueta = '"+idEtiqueta+"' where id = "+compraLojaVirtual.getId()+";");
		}
		
		//vd_Cp_Loja_virt_repository.updateEtiqueta(idEtiqueta, compraLojaVirtual.getId());
		// Faz a compra da etiqueta
		OkHttpClient clientCompra = new OkHttpClient();
		okhttp3.MediaType mediaTypeC = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyC = okhttp3.RequestBody.create(mediaTypeC, "{\"orders\":[\""+idEtiqueta+"\"]}");
		okhttp3.Request requestC = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/checkout")
		  .post(bodyC)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response responseC = clientCompra.newCall(requestC).execute();
		
		if (!responseC.isSuccessful()) {
			return new ResponseEntity<String>("Não foi possivel realizar a compra da etiqueta", HttpStatus.OK);
		}
		
		// Gera as etiquetas
		OkHttpClient clientGe = new OkHttpClient();
		okhttp3.MediaType mediaTypeGe = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyGe = okhttp3.RequestBody.create(mediaTypeGe, "{\"orders\":[\""+idEtiqueta+"\"]}");
		okhttp3.Request requestGe = new okhttp3.Request.Builder()
		    .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/generate")
			.post(bodyGe)
			.addHeader("Accept", "application/json")
			.addHeader("Content-Type", "application/json")
			.addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
			.addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
			.build();

		Response responseGe = clientGe.newCall(requestGe).execute();
		
		if (!responseGe.isSuccessful()) {
			return new ResponseEntity<String>("Não foi possivel gerar a etiqueta ", HttpStatus.OK);
		}
		// Faz a impressão da etiquta
		OkHttpClient clientIm = new OkHttpClient();
		okhttp3.MediaType mediaTypeIm = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyIm = okhttp3.RequestBody.create(mediaTypeIm, "{\"orders\":[\""+idEtiqueta+"\"]}");
		okhttp3.Request requestIm = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/print")
		  .post(bodyIm)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		Response responseIm = clientIm.newCall(requestIm).execute();
		
		if (!responseIm.isSuccessful()) {
			return new ResponseEntity<String>("Não foi possivel imprimir a Etiqueta ", HttpStatus.OK);
		}
		
		String urlEtiqueta = responseIm.body().string();
		if (urlEtiqueta != null && !urlEtiqueta.isEmpty() && compraLojaVirtual.getId() != null) {
			jdbcTemplate.execute("begin; update vd_cp_loja_virt set url_imprime_etiqueta = '"+urlEtiqueta+"' where id = '"+compraLojaVirtual.getId()+"' ;commit;");
		}
		//		vd_Cp_Loja_virt_repository.updateUrlEtiqueta(urlEtiqueta, compraLojaVirtual.getId());
		
		return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteVendaTotalBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable(value = "idVenda") Long idVenda) {
		vendaService.exclusaoTotalVendaBanco(idVenda);
		return new ResponseEntity<String>("Venda excluida com sucesso",HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteVendaTotalBanco2/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco2(@PathVariable(value = "idVenda") Long idVenda) {
		vendaService.exclusaoTotalVendaBanco2(idVenda);
		return new ResponseEntity<String>("Venda excluida logicamente com sucesso!",HttpStatus.OK);
	}
	
	@ResponseBody
	@PutMapping(value = "/ativaRegistroVendaBanco/{idVenda}")
	public ResponseEntity<String> ativaRegistroVendaBanco(@PathVariable(value = "idVenda") Long idVenda) {
		vendaService.ativaRegistroVendaBanco(idVenda);
		return new ResponseEntity<String>("Venda ativada com sucesso!",HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaDinamicaFaixaData/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamicaFaixaData(
				@PathVariable("data1") String data1, @PathVariable("data2") String data2) throws ParseException {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = null;
		
		compraLojaVirtual = vendaService.consultaVendaFaixaData(data1, data2);
		
		if(compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
			
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			
			compraLojaVirtualDTO.setValorDesc(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId()); 
			
			for (ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamica(@PathVariable("valor") String valor,
													@PathVariable("tipoconsulta") String tipoconsulta) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = null;
		
		if (tipoconsulta.equalsIgnoreCase("POR_ID_PROD")) {
			compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorProduto(Long.parseLong(valor));
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_PROD")) {
			compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorNomeProduto(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
			compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorNomeCliente(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_COBRANCA")) {
			compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorEnderecoCobranca(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_ENDERECO_ENTREGA")) {
			compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorEnderecoEntrega(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_CPF")) {
			compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorCpfCliente(valor.toUpperCase().trim());
		}
			
		if(compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
			
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			
			compraLojaVirtualDTO.setValorDesc(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId()); 
			
			for (ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProdutoId(@PathVariable("id") Long idProd ) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorProduto(idProd);
		
		if(compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
			
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			
			compraLojaVirtualDTO.setValorDesc(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId()); 
			
			for (ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/vendaPorCliente/{idCliente}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> vendaPorCliente(@PathVariable("idCliente") Long idCliente) {
		
		List<VendaCompraLojaVirtual> compraLojaVirtual = vd_Cp_Loja_virt_repository.vendaPorCliente(idCliente);
		
		if(compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {
			
			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			compraLojaVirtualDTO.setValorTotal(vcl.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vcl.getPessoa());
			
			compraLojaVirtualDTO.setCobranca(vcl.getEnderecoCobranca());
			compraLojaVirtualDTO.setEntrega(vcl.getEnderecoEntrega());
			
			compraLojaVirtualDTO.setValorDesc(vcl.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vcl.getValorFrete());
			compraLojaVirtualDTO.setId(vcl.getId()); 
			
			for (ItemVendaLoja item : vcl.getItemVendaLojas()) {
				
				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());
				
				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
			}
			
			compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
		}
		
		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/consultarFreteLojaVirtual")
	public ResponseEntity<List<EmpresaTransporteDTO>> consultaFrete(@RequestBody @Valid  ConsultaFreteDTO consultaFreteDTO) throws Exception {
	
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(consultaFreteDTO);
		
		OkHttpClient client = new OkHttpClient();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
		// instancia objeto de requisição
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(ApiTokenIntegracao.URL_TOKEN_MELHR_ENVIO_SAND_BOX + "api/v2/me/shipment/calculate")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
		  .addHeader("User-Agent", "silvaarthur.pereira123@gmail.com")
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
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
		
		return new ResponseEntity<List<EmpresaTransporteDTO>>(empresaTransporteDTOs ,HttpStatus.OK);
	}
	
}
