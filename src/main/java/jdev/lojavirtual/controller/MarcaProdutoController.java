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

import jdev.lojavirtual.ExceptionLojaVirtualJava;
import jdev.lojavirtual.model.MarcaProduto;
import jdev.lojavirtual.repository.MarcaRepository;

@Controller
public class MarcaProdutoController {

	@Autowired
	private MarcaRepository marcaRepository;

	@ResponseBody
	@PostMapping(value = "/salvarMarcaProduto")
	public ResponseEntity<MarcaProduto> salvarMarcaProduto(@RequestBody @Valid MarcaProduto marcaProduto)
			throws ExceptionLojaVirtualJava {

		if (marcaProduto.getId() == null) {
			List<MarcaProduto> marcaProdutos = marcaRepository
					.buscarMarcaDesc(marcaProduto.getNomeDesc().toUpperCase());

			if (!marcaProdutos.isEmpty()) {
				throw new ExceptionLojaVirtualJava("Já existe marca com a descrição: " + marcaProduto.getNomeDesc());
			}
		}

		MarcaProduto marcaProdutoSalvo = marcaRepository.save(marcaProduto);
		return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/deleteMarcaProduto")
	public ResponseEntity<?> deleteMarcaProduto(@RequestBody MarcaProduto marcaProduto) {
		marcaRepository.deleteById(marcaProduto.getId());
		return new ResponseEntity("Marca produto Removido", HttpStatus.OK);
	}

//	@Secured({ "ROLE_GERENTE", "ROLE_ADMIN" })
	@ResponseBody
	@DeleteMapping(value = "/deleteMarcaProdutoPorId/{id}")
	public ResponseEntity<?> deleteMarcaProdutoPorId(@PathVariable("id") Long id) {
		marcaRepository.deleteById(id);
		return new ResponseEntity("Marca produto Removido", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/obterMarcaProduto/{id}")
	public ResponseEntity<MarcaProduto> obterMarcaProduto(@PathVariable("id") Long id) throws ExceptionLojaVirtualJava {
		MarcaProduto marcaProduto = marcaRepository.findById(id).orElse(null);

		if (marcaProduto == null) {
			throw new ExceptionLojaVirtualJava("Não encotrou Marca Produto com código: " + id);
		}

		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/buscarMarcaProdutoPorDesc/{desc}")
	public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorDesc(@PathVariable("desc") String desc) {
		List<MarcaProduto> marcaProdutos = marcaRepository.buscarMarcaDesc(desc.toUpperCase());
		return new ResponseEntity<List<MarcaProduto>>(marcaProdutos, HttpStatus.OK);
	}
}
