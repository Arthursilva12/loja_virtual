package jdev.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.ExceptionLojaVirtualJava;
import jdev.lojavirtual.model.CategoriaProduto;
import jdev.lojavirtual.model.dto.CategoriaProdutoDTO;
import jdev.lojavirtual.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository; 
	
	
	@ResponseBody
	@GetMapping(value = "/buscarPorDescCategoria/{desc}") 
	public ResponseEntity<List<CategoriaProduto>> buscarPorDesc(@PathVariable("desc") String desc) {
		List<CategoriaProduto> produtos = categoriaProdutoRepository.buscarCategoriaDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<CategoriaProduto>>(produtos,HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "/deleteCategoria") 
	public ResponseEntity<?> deleteCategoria(@RequestBody CategoriaProduto categoriaProduto) {
		
		if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
			return new ResponseEntity("Categoria já foi removida",HttpStatus.OK);
		}
		
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());
		return new ResponseEntity("Categoria Removida",HttpStatus.OK);
	}

	
	@ResponseBody
	@PostMapping(value = "/salvarCategoria")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionLojaVirtualJava {
		
		if(categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {
			throw new ExceptionLojaVirtualJava("A empresa deve ser informada.");
		}
		
		if(categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc().toUpperCase())) {
			throw new ExceptionLojaVirtualJava("Nâo pode cadastrar categoria com o mesmo nome.");
		}
		
		
		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
		
		CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
		categoriaProdutoDTO.setId(categoriaSalva.getId());
		categoriaProdutoDTO.setNomeDesc(categoriaSalva.getNomeDesc());
		categoriaProdutoDTO.setEmpresa(categoriaSalva.getEmpresa().getId().toString());
		
		return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);
	}
	
	
}
