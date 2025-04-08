package jdev.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

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
import jdev.lojavirtual.enums.TipoPessoa;
import jdev.lojavirtual.model.Endereco;
import jdev.lojavirtual.model.PessoaFisica;
import jdev.lojavirtual.model.PessoaJuridica;
import jdev.lojavirtual.model.dto.CepDTO;
import jdev.lojavirtual.model.dto.ConsultaCnpjDto;
import jdev.lojavirtual.repository.EnderecoRepository;
import jdev.lojavirtual.repository.PessoaFisicaRepository;
import jdev.lojavirtual.repository.PessoaRepository;
import jdev.lojavirtual.service.PessoaUserService;
import jdev.lojavirtual.service.ServiceContagemAcessoApi;
import jdev.lojavirtual.util.ValidaCNPJ;
import jdev.lojavirtual.util.ValidaCPF;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private ServiceContagemAcessoApi contagemAcessoApi;
	
	@ResponseBody
	@GetMapping(value = "/consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome") String nome){
		List<PessoaFisica> pessoPf = pessoaFisicaRepository.pesquisaPorNomePf(nome.trim().toUpperCase());
		contagemAcessoApi.atualizaAcessoEndPointPf();
		return new ResponseEntity<List<PessoaFisica>>(pessoPf, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaPfCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPfCpf(@PathVariable("cpf") String cpf){
		List<PessoaFisica> pessoPfCpf = pessoaFisicaRepository.existeCpfCadastradoList(cpf);
		return new ResponseEntity<List<PessoaFisica>>(pessoPfCpf, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaNomePj/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePj(@PathVariable("nome") String nome){
		List<PessoaJuridica> pessoNomePj = pessoaRepository.pesquisaPorNomePj(nome.trim().toUpperCase());
		return new ResponseEntity<List<PessoaJuridica>>(pessoNomePj, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaCnpj/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaCnpj(@PathVariable("cnpj") String cnpj){
		List<PessoaJuridica> consultaCnpj = pessoaRepository.existeCnpjCadastradoList(cnpj.trim().toUpperCase());
		return new ResponseEntity<List<PessoaJuridica>>(consultaCnpj, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
		return new ResponseEntity<CepDTO>(pessoaUserService.consultaCep(cep), HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDto> consultaCnpjReceitaWs(@PathVariable("cnpj") String cnpj){
		return new ResponseEntity<ConsultaCnpjDto>(pessoaUserService.consultaCnpjReceitaWs(cnpj), HttpStatus.OK);
	}
	
	// end-point, microservice e uma API se for liberar
	@ResponseBody
	@PostMapping(value = "/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtualJava{
		
//		if(pessoaJuridica.getNome() == null || pessoaJuridica.getNome().trim().isEmpty()) {
//			throw new ExceptionLojaVirtualJava("Informe o campo de nome");
//		}
		
		if(pessoaJuridica == null) {
			throw new ExceptionLojaVirtualJava("Pessoa juridica não pode ser null");
		}
		
		if(pessoaJuridica.getTipoPessoa() == null) {
			throw new ExceptionLojaVirtualJava("Informe o tipo Jurídico ou Fornecedor da Loja");
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
		
		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
			
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
			}
		}else {
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
				
				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
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
		
		if(pessoaFisica.getTipoPessoa() == null) {
			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
		}
		
		if(pessoaFisica.getId() == null && pessoaFisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLojaVirtualJava("Já existe CPF com o número: " + pessoaFisica.getCpf());
		}
		
		if(!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLojaVirtualJava("CPF: " + pessoaFisica.getCpf() + " está inválido");
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
	
}
