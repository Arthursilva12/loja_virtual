package jdev.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import jdev.lojavirtual.controller.PessoaController;
import jdev.lojavirtual.enums.TipoEndereco;
import jdev.lojavirtual.model.Endereco;
import jdev.lojavirtual.model.PessoaFisica;
import jdev.lojavirtual.model.PessoaJuridica;
import jdev.lojavirtual.repository.PessoaRepository;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase{


	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaJuridica() throws ExceptionLojaVirtualJava {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setNome("Arthursilvatesteeee");
		pessoaJuridica.setEmail("silvaarthurtesteee@gmail.com");
//		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setCnpj("92.542.979/0001-48");
		pessoaJuridica.setTelefone("619830573");
		pessoaJuridica.setInscEstadual("906789678");
		pessoaJuridica.setInscMunicipal("435347890558955345345");
		pessoaJuridica.setNomeFantasia("fdgdfsgfgd");
		pessoaJuridica.setRazaoSocial("fdghsrfghjdfgh");
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Dalva 7 ghsdfsdg");
		endereco1.setCep("7325667567346");
		endereco1.setCidade("luziânia");
		endereco1.setComplemento("Arvore grande");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setLogradouro("redfhd");
		endereco1.setNumero("15");
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setRua("rua 17");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("GO");
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Dalva 8 fgfsdfsdf");
		endereco2.setCep("546565675645");
		endereco2.setCidade("luziânia");
		endereco2.setComplemento("casa verde");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setLogradouro("dfsdfs");
		endereco2.setNumero("23");
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setRua("rua 63");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("GO");
		
		pessoaJuridica.getEnderecos().add(endereco1);
		pessoaJuridica.getEnderecos().add(endereco2);
		
		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);// valida se foi gerado a PK
		}
		// test se veio os 2 endereços
		assertEquals(2, pessoaJuridica.getEnderecos().size());
	}
	
	
	@Test
	public void testCadPessoaFisica() throws ExceptionLojaVirtualJava {
		
		PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpjCadastrado("1741926376295");
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setNome("Arthursilvateste");
		pessoaFisica.setEmail("silvaarthurteste@gmail.com");
		pessoaFisica.setCpf("665.265.490-39");
		pessoaFisica.setTelefone("619830573");
		pessoaFisica.setEmpresa(pessoaJuridica);
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Dalva 7 ghsdfsdg");
		endereco1.setCep("7325667567346");
		endereco1.setCidade("luziânia");
		endereco1.setComplemento("Arvore grande");
		endereco1.setLogradouro("redfhd");
		endereco1.setNumero("15");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setRua("rua 17");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("GO");
		endereco1.setEmpresa(pessoaJuridica);
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Dalva 8 fgfsdfsdf");
		endereco2.setCep("546565675645");
		endereco2.setCidade("luziânia");
		endereco2.setComplemento("casa verde");
		endereco2.setEmpresa(pessoaFisica);
		endereco2.setLogradouro("dfsdfs");
		endereco2.setNumero("23");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setRua("rua 63");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("GO");
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco1);
		pessoaFisica.getEnderecos().add(endereco2);
		
		pessoaFisica = pessoaController.salvarPf(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0);
		
		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);// valida se foi gerado a PK
		}
		// test se veio os 2 endereços
		assertEquals(2, pessoaFisica.getEnderecos().size());
	}
	
}
