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
import jdev.lojavirtual.model.ContaPagar;
import jdev.lojavirtual.repository.ContaPagarRepository;

@Controller
@RestController
public class ContaPagarController {

	@Autowired
	private ContaPagarRepository contaPagarRepository;
	
	@ResponseBody
	@PostMapping(value = "/salvarContaPagar")
	public ResponseEntity<ContaPagar> salvarProduto(@RequestBody @Valid ContaPagar contaPagar) throws ExceptionLojaVirtualJava {

		if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("A empresa deve ser informada");
		}
		
		if (contaPagar.getPessoa() == null || contaPagar.getPessoa().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("Pessoa responsavel deve ser informada");
		}
		
		if (contaPagar.getPessoa_fornecedor() == null || contaPagar.getPessoa_fornecedor().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("Fornecedor resposável deve ser informado");
		}
		
		if (contaPagar.getId() == null) {
			List<ContaPagar> contaPagars = contaPagarRepository.buscaContaDesc(contaPagar.getDescricao().toUpperCase().trim());
			
			if (!contaPagars.isEmpty()) {
				throw new ExceptionLojaVirtualJava("Já existe conta a pagar com a mesma descrição");
			}
		}
		
		ContaPagar contaPagarSalvo = contaPagarRepository.save(contaPagar);
		return new ResponseEntity<ContaPagar>(contaPagarSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/deleteContaPagar") 
	public ResponseEntity<String> deleteContaPagar(@RequestBody ContaPagar contaPagar) {
		contaPagarRepository.deleteById(contaPagar.getId());
		return new ResponseEntity<String>("Conta a Pagar Removido",HttpStatus.OK);
	}
	
	
//	@Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
	@ResponseBody
	@DeleteMapping(value = "/deleteContaPagarPorId/{id}") 
	public ResponseEntity<String>deleteContaPagarPorId(@PathVariable("id") Long id) {
		contaPagarRepository.deleteById(id);
		return new ResponseEntity<String>("Conta Pagar Removido",HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/obterContaPagar/{id}") 
	public ResponseEntity<ContaPagar> obterContaPagar(@PathVariable("id") Long id) throws ExceptionLojaVirtualJava {
		ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);
		
		if(contaPagar == null) {
			throw new ExceptionLojaVirtualJava("Não encotrou Conta a Pagar com código: " + id);
		}
		
		return new ResponseEntity<ContaPagar>(contaPagar,HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/buscarContaPagarDesc/{desc}") 
	public ResponseEntity<List<ContaPagar>> buscarContaPagarDesc(@PathVariable("desc") String desc) {
		List<ContaPagar> contaPagar = contaPagarRepository.buscaContaDesc(desc.toUpperCase());
		return new ResponseEntity<List<ContaPagar>>(contaPagar,HttpStatus.OK);
	}
}
