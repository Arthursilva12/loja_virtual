package jdev.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.ExceptionLojaVirtualJava;
import jdev.lojavirtual.model.AvaliacaoProduto;
import jdev.lojavirtual.repository.AvaliacaoProdutoRepository;

@RestController
public class AvaliacaoProdutoController {

	
	@Autowired
	private AvaliacaoProdutoRepository avaliacaoProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarAvaliacaoProduto")
	public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionLojaVirtualJava {
		
		if (avaliacaoProduto.getEmpresa() == null || (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() <= 0)) {
			throw new ExceptionLojaVirtualJava("Informe a empresa dona do registro");
		}
		
		if (avaliacaoProduto.getProduto() == null || (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() <= 0)) {
			throw new ExceptionLojaVirtualJava("Avaliação deve conter o produto associado");
		}
		
		if (avaliacaoProduto.getPessoa() == null || (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() <= 0)) {
			throw new ExceptionLojaVirtualJava("A avaliação deve conter a pessoa ou cliente associado");
		}
		
		avaliacaoProduto = avaliacaoProdutoRepository.saveAndFlush(avaliacaoProduto);
		return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteAvaliacaoPessoa/{idAvaliacao}") 
	public ResponseEntity<?> deleteAvaliacaoPessoa(@PathVariable("idAvaliacao") Long idAvaliacao) {
		avaliacaoProdutoRepository.deleteById(idAvaliacao);
		return new ResponseEntity<String>("Avaliação Removida",HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/AvaliacaoProduto/{idProduto}") 
	public ResponseEntity<List<AvaliacaoProduto>> AvaliacaoProduto(@PathVariable("idProduto") Long idProduto) {
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProduto(idProduto);
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos,HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/AvaliacaoProdutoPessoa/{idProduto}/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProduto>> AvaliacaoProdutoPessoa(
			@PathVariable("idProduto") Long idProduto, @PathVariable("idPessoa") Long idPessoa) {
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoProdutoPessoa(idProduto,idPessoa);
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/AvaliacaoPessoa/{idPessoa}") 
	public ResponseEntity<List<AvaliacaoProduto>> AvaliacaoPessoa(@PathVariable("idPessoa") Long idPessoa) {
		List<AvaliacaoProduto> avaliacaoProdutos = avaliacaoProdutoRepository.avaliacaoPessoa(idPessoa);
		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacaoProdutos,HttpStatus.OK);
	}
	
}
