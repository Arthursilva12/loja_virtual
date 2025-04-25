package jdev.lojavirtual.controller;

import java.util.ArrayList;
import java.util.List;

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

import jdev.lojavirtual.model.ImagemProduto;
import jdev.lojavirtual.model.dto.ImagemProdutoDTO;
import jdev.lojavirtual.repository.ImagemProdutoRepository;

@RestController
public class ImagemProdutoController {

	
	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;
	
	
	@ResponseBody
	@PostMapping(value = "/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody ImagemProduto imagemProduto){
		imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);
		
		ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
		imagemProdutoDTO.setId(imagemProduto.getId());
		imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
		imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
		imagemProdutoDTO.setImagem_original(imagemProduto.getImagem_original());
		imagemProdutoDTO.setImagem_miniatura(imagemProduto.getImagem_miniatura());
		
		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteTodasImagens/{id}")
	public ResponseEntity<?>deleteTodasImagens(@RequestBody ImagemProduto idProduto) {
		imagemProdutoRepository.deleteImagens(idProduto.getId());
		return new ResponseEntity<String>("Imagens do produto removida", HttpStatus.OK);
	}
	
	@ResponseBody// deleta imagem produto passando json
	@DeleteMapping(value = "/deleteImagemObjeto")
	public ResponseEntity<?>deleteImagemObjeto(@RequestBody ImagemProduto imgProduto) {
		
		if (!imagemProdutoRepository.existsById(imgProduto.getId())) {
			return new ResponseEntity<String>("Imagem já foi removida ou não existe com esse id: " + imgProduto.getId(), HttpStatus.OK);
		}
		
		imagemProdutoRepository.deleteById(imgProduto.getId());
		return new ResponseEntity<String>("Imagem removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "/deleteImagemProduto/{id}")
	public ResponseEntity<?>deleteImagemProduto(@PathVariable("id") Long id) {
		
		if (!imagemProdutoRepository.existsById(id)) {
			return new ResponseEntity<String>("Imagem já foi removida ou não existe com esse id: " + id, HttpStatus.OK);
		}
		
		imagemProdutoRepository.deleteById(id);
		return new ResponseEntity<String>("Imagem removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/obterImagemPorProduto/{idProduto}")
	public ResponseEntity<List<ImagemProdutoDTO>> obterImagemPorProduto(@PathVariable("idProduto") Long idProduto) {
		
		List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();
		List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(idProduto);
		
		for (ImagemProduto imagemProduto : imagemProdutos) {
			
			ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
			imagemProdutoDTO.setId(imagemProduto.getId());
			imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
			imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
			imagemProdutoDTO.setImagem_original(imagemProduto.getImagem_original());
			imagemProdutoDTO.setImagem_miniatura(imagemProduto.getImagem_miniatura());
			
			dtos.add(imagemProdutoDTO);
			
		}
		
		return new ResponseEntity<List<ImagemProdutoDTO>>(dtos,HttpStatus.OK);
	}
	
}
