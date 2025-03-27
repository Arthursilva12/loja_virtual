package jdev.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.ExceptionLojaVirtualJava;
import jdev.lojavirtual.model.PessoaFisica;
import jdev.lojavirtual.model.PessoaJuridica;
import jdev.lojavirtual.repository.PessoaRepository;
import jdev.lojavirtual.service.PessoaUserService;
import jdev.lojavirtual.util.ValidaCNPJ;
import jdev.lojavirtual.util.ValidaCPF;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	PessoaUserService pessoaUserService;
	
	// end-point, microservice e uma API se for liberar
	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtualJava{
		
		if(pessoaJuridica == null) {
			throw new ExceptionLojaVirtualJava("Pessoa juridica não pode ser null");
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLojaVirtualJava("Já existe CNPJ com o número: " + pessoaJuridica.getCnpj());
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionLojaVirtualJava("Já existe Inscrição estadual cadastrado com o número: " + pessoaJuridica.getInscEstadual());
		}
		
		if(!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionLojaVirtualJava("Cnpj: " + pessoaJuridica.getCnpj() + " está inválido");
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionLojaVirtualJava{
		
		if(pessoaFisica == null) {
			throw new ExceptionLojaVirtualJava("Pessoa fisica não pode ser null");
		}
		
		if(pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLojaVirtualJava("Já existe CPF com o número: " + pessoaFisica.getCpf());
		}
		
		if(!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLojaVirtualJava("CPF: " + pessoaFisica.getCpf() + " está inválido");
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
	
}
