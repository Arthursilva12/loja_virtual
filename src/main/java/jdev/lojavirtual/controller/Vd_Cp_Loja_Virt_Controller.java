package jdev.lojavirtual.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.ExceptionLojaVirtualJava;
import jdev.lojavirtual.model.Endereco;
import jdev.lojavirtual.model.ItemVendaLoja;
import jdev.lojavirtual.model.PessoaFisica;
import jdev.lojavirtual.model.StatusRastreio;
import jdev.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.lojavirtual.model.dto.ItemVendaDTO;
import jdev.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import jdev.lojavirtual.repository.EnderecoRepository;
import jdev.lojavirtual.repository.NotaFiscalVendaRepository;
import jdev.lojavirtual.repository.StatusRastreioRepository;
import jdev.lojavirtual.repository.Vd_Cp_Loja_virt_repository;
import jdev.lojavirtual.service.VendaService;

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
	
	@ResponseBody
	@PostMapping(value = "/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtualJava {
		
		
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
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date d1 = dateFormat.parse(data1);
		Date d2 = dateFormat.parse(data2);
		
		
		compraLojaVirtual = vd_Cp_Loja_virt_repository.consultaVendaFaixaData(d1, d2);
		
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
}
