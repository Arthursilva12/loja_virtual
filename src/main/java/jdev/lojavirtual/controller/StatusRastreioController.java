package jdev.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.model.StatusRastreio;
import jdev.lojavirtual.repository.StatusRastreioRepository;

@RestController
public class StatusRastreioController {

	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@ResponseBody
	@GetMapping(value = "/listRastreio/{idVenda}")
	public ResponseEntity<List<StatusRastreio>> listRastreio(@PathVariable("idVenda") Long idVenda) {
		
		List<StatusRastreio> statusRastreios = statusRastreioRepository.listRastreioVenda(idVenda);
		return new ResponseEntity<List<StatusRastreio>>(statusRastreios,HttpStatus.OK);
	}
	
}
