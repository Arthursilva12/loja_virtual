package jdev.lojavirtual.controller;

import java.util.ArrayList;
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
import jdev.lojavirtual.model.NotaFiscalCompra;
import jdev.lojavirtual.model.NotaFiscalVenda;
import jdev.lojavirtual.model.dto.ObjetoRelatorioStatusCompraDTO;
import jdev.lojavirtual.model.dto.ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO;
import jdev.lojavirtual.model.dto.ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO;
import jdev.lojavirtual.repository.NotaFiscalCompraRepository;
import jdev.lojavirtual.repository.NotaFiscalVendaRepository;
import jdev.lojavirtual.service.NotaFiscalCompraService;

@RestController
public class NotaFiscalCompraController {

	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private NotaFiscalCompraService notaFiscalCompraService;
	
	@ResponseBody
	@PostMapping(value = "/relatorioStatusCompra")
	public ResponseEntity<List<ObjetoRelatorioStatusCompraDTO>> relatorioStatusCompra(@Valid 
					@RequestBody ObjetoRelatorioStatusCompraDTO objetoRelatorioStatusCompraDTO) {
		
		List<ObjetoRelatorioStatusCompraDTO> retorno = new ArrayList<ObjetoRelatorioStatusCompraDTO>();
		retorno = notaFiscalCompraService.gerarRelatorioStatusVendaLojaVirtual(objetoRelatorioStatusCompraDTO);
		
		return new ResponseEntity<List<ObjetoRelatorioStatusCompraDTO>>(retorno,HttpStatus.OK);
	}
	
	
	@ResponseBody
    @PostMapping(value = "/relatorioProdutoCompradoNotaFiscal")
	public ResponseEntity<List<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO>> relatorioProdutoCompradoNotaFiscal
		(@Valid @RequestBody ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO objetoRequisicaoRelatorioProdutoNotaFiscalDto) {
		
		List<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO> retorno = new ArrayList<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO>();
		retorno = notaFiscalCompraService.gerarRelatorioProdCompraNota(objetoRequisicaoRelatorioProdutoNotaFiscalDto);
		
		return new ResponseEntity<List<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO>>(retorno, HttpStatus.OK);
	}
	
	
	@ResponseBody
    @PostMapping(value = "/relatorioProdEstoque")
	public ResponseEntity<List<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO>> relatorioProdEstoque
						(@Valid @RequestBody ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO alertaEstoqueBaixoDTO) {
		
		List<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO> retorno = 
					new ArrayList<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO>();
		
		retorno = notaFiscalCompraService.gerarRelatorioAlertEstoque(alertaEstoqueBaixoDTO);
		return new ResponseEntity<List<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO>>(retorno, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionLojaVirtualJava {

		if (notaFiscalCompra.getId() == null) {
			
			if (notaFiscalCompra.getDescaoObs() != null) {
				List<NotaFiscalCompra> fiscalCompras = notaFiscalCompraRepository
						.buscarNotaPorDesc(notaFiscalCompra.getDescaoObs().toUpperCase().trim());
				
				if (!fiscalCompras.isEmpty()) {
					throw new ExceptionLojaVirtualJava("Já existe Nota de compra com essa mesma descrição: " + notaFiscalCompra.getDescaoObs());
				}
				
			}
		}

		if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("A pessoa juridica da nota fiscal deve ser informada");
		}
		
		if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("A empresa deve ser informada");
		}
		
		if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
			throw new ExceptionLojaVirtualJava("A conta pagar da nota deve ser informada");
		}
		
		NotaFiscalCompra NotaFiscalCompraSalvo = notaFiscalCompraRepository.save(notaFiscalCompra);
		
		return new ResponseEntity<NotaFiscalCompra>(NotaFiscalCompraSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "/deleteNotaFiscalCompraPorId/{id}")
	public ResponseEntity<?> deleteNotaFiscalCompraPorId(@PathVariable("id") Long id) {
		notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);// deleta os filhos
		notaFiscalCompraRepository.deleteById(id);// deleta o pai
		return new ResponseEntity("Nota Fiscal Compra Removido", HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/obterNotaFiscalCompra/{id}")
	public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionLojaVirtualJava {
		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

		if (notaFiscalCompra == null) {
			throw new ExceptionLojaVirtualJava("Não encotrou Nota Fiscal com código: " + id);
		}

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
	}
	
	
	@ResponseBody// retorna uma lista
	@GetMapping(value = "/obterNotaFiscalCompraDaVenda/{idVenda}")
	public ResponseEntity<List<NotaFiscalVenda>> obterNotaFiscalCompraDaVenda(@PathVariable("idVenda") Long idVenda) throws ExceptionLojaVirtualJava {
		List<NotaFiscalVenda> notaFiscalCompra = notaFiscalVendaRepository.buscaNotaPorVenda(idVenda);

		if (notaFiscalCompra == null) {
			throw new ExceptionLojaVirtualJava("Não encotrou nota fiscal de venda com código da venda: " + idVenda);
		}

		return new ResponseEntity<List<NotaFiscalVenda>>(notaFiscalCompra, HttpStatus.OK);
	}
	
	
	@ResponseBody// retorna um objeto
	@GetMapping(value = "/obterNotaFiscalCompraDaVendaUnico/{idVenda}")
	public ResponseEntity<NotaFiscalVenda> obterNotaFiscalCompraDaVendaUnico(@PathVariable("idVenda") Long idVenda) throws ExceptionLojaVirtualJava {
		NotaFiscalVenda notaFiscalCompra = notaFiscalVendaRepository.buscaNotaPorVendaUnica(idVenda);

		if (notaFiscalCompra == null) {
			throw new ExceptionLojaVirtualJava("Não encotrou nota fiscal de venda com código da venda: " + idVenda);
		}

		return new ResponseEntity<NotaFiscalVenda>(notaFiscalCompra, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "/buscarNotaFiscalPorDesc/{desc}")
	public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalPorDesc(@PathVariable("desc") String desc) {
		List<NotaFiscalCompra> marcaProdutos = notaFiscalCompraRepository.buscarNotaPorDesc(desc.toUpperCase());
		return new ResponseEntity<List<NotaFiscalCompra>>(marcaProdutos, HttpStatus.OK);
	}
	
}
