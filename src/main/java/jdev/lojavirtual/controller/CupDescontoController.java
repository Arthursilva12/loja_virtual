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
import jdev.lojavirtual.model.CupDesc;
import jdev.lojavirtual.model.MarcaProduto;
import jdev.lojavirtual.repository.CupDescontoRepository;

@RestController
public class CupDescontoController {

	@Autowired
	private CupDescontoRepository cupDescontoRepository;
	
	
	@ResponseBody
	@PostMapping(value = "/salvarCupDesc")
	public ResponseEntity<CupDesc> salvarCupDesc(@RequestBody @Valid CupDesc cupDesc) throws ExceptionLojaVirtualJava {
		CupDesc cupDesc2 = cupDescontoRepository.save(cupDesc);
		return new ResponseEntity<CupDesc>(cupDesc2,HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteCupDesc/{idCupDesc}")
	public ResponseEntity<CupDesc> deleteCupDesc(@PathVariable(value = "idCupDesc") Long idCupDesc) {
		cupDescontoRepository.deleteById(idCupDesc);
		return new ResponseEntity("Cupom excluido com sucesso",HttpStatus.OK);
	}
	
 	
	@ResponseBody
	@GetMapping(value = "/obterCupom/{id}")
	public ResponseEntity<CupDesc> obterCupom(@PathVariable("id") Long id) throws ExceptionLojaVirtualJava {
		CupDesc cupDesc = cupDescontoRepository.findById(id).orElse(null);

		if (cupDesc == null) {
			throw new ExceptionLojaVirtualJava("Não encotrou cupom de desconto com código: " + id);
		}

		return new ResponseEntity<CupDesc>(cupDesc, HttpStatus.OK);
	}

	
	@ResponseBody
	@GetMapping(value = "/listaCupomDesc")
	public ResponseEntity<List<CupDesc>> listaCupomDesc() {
		
		return new ResponseEntity<List<CupDesc>>(cupDescontoRepository.findAll(), HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/listaCupomDesc/{idEmpresa}")
	public ResponseEntity<List<CupDesc>> listaCupomDesc(@PathVariable("idEmpresa") Long idEmpresa) {
		
		return new ResponseEntity<List<CupDesc>>(cupDescontoRepository.cupDescontoPorEmpresa(idEmpresa), HttpStatus.OK);
	}
	
}
