package jdev.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import jdev.lojavirtual.model.PessoaJuridica;
import jdev.lojavirtual.repository.PessoaRepository;
import jdev.lojavirtual.service.PessoaUserService;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase{

	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaFisica() {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setNome("Arthur");
		pessoaJuridica.setEmail("Arthur@gmail.com");
		pessoaJuridica.setCnpj("4564356435656");
		pessoaJuridica.setTelefone("619830573");
		pessoaJuridica.setInscEstadual("3453453453453");
		pessoaJuridica.setInscMunicipal("435345345345");
		pessoaJuridica.setNomeFantasia("fdgdfsgfgd");
		pessoaJuridica.setRazaoSocial("fdghsrfghjdfgh");
		
		pessoaRepository.save(pessoaJuridica);
		
//		PessoaFisica pessoaFisica = new PessoaFisica();
//		pessoaFisica.setNome("Arthur");
//		pessoaFisica.setEmail("Arthur@gmail.com");
//		pessoaFisica.setCpf("4564356435656");
//		pessoaFisica.setTelefone("619830573");
//		
//		pessoaFisica.setEmpresa(pessoaFisica);
	}
	
}
