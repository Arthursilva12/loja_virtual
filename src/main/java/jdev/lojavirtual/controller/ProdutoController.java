package jdev.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.ExceptionLojaVirtualJava;
import jdev.lojavirtual.model.Produto;
import jdev.lojavirtual.repository.ProdutoRepository;

@Controller
@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtualJava {

		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("A empresa deve ser informada");
		}
		
		if (produto.getId() == null) {
			List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());
			
			if (!produtos.isEmpty()) {
				throw new ExceptionLojaVirtualJava("Já existe Produto com o nome: " + produto.getNome());
			}
		}

		if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("A categoria deve ser informada");
		}
		
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("A marca deve ser informada");
		}
		
		Produto produtoSalvo = produtoRepository.save(produto);
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/deleteProduto") 
	public ResponseEntity<String> deletedeleteProdutoAcesso(@RequestBody Produto produto) {
		produtoRepository.deleteById(produto.getId());
		return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
	}
	
	
//	@Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
	@ResponseBody
	@DeleteMapping(value = "/deleteProdutoPorId/{id}") 
	public ResponseEntity<String>deleteAcessoPorId(@PathVariable("id") Long id) {
		produtoRepository.deleteById(id);
		return new ResponseEntity<String>("Acesso Removido",HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/obterProduto/{id}") 
	public ResponseEntity<Produto> obterAcesso(@PathVariable("id") Long id) throws ExceptionLojaVirtualJava {
		Produto produto = produtoRepository.findById(id).orElse(null);
		
		if(produto == null) {
			throw new ExceptionLojaVirtualJava("Não encotrou produto com código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto,HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/buscarProdutoNome/{desc}") 
	public ResponseEntity<List<Produto>> buscarPorDesc(@PathVariable("desc") String desc) {
		List<Produto> produto = produtoRepository.buscarProdutoNome(desc.toUpperCase());
		return new ResponseEntity<List<Produto>>(produto,HttpStatus.OK);
	}
}
