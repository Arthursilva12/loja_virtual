package jdev.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.model.FormaPagamento;
import jdev.lojavirtual.repository.FormaPagamentoRepository;

@RestController
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	
	
	@ResponseBody
	@PostMapping(value = "/SalvarFormaPagamento")
	public ResponseEntity<FormaPagamento> SalvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) {
		
		
		
		
		formaPagamento = formaPagamentoRepository.save(formaPagamento);
		return new ResponseEntity<FormaPagamento>(formaPagamento,HttpStatus.OK);
	}
	
}
