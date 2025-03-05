package jdev.lojavirtual;

import java.util.Calendar;
import java.util.Date;

import javax.xml.crypto.Data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import jdev.lojavirtual.controller.PessoaController;
import jdev.lojavirtual.model.PessoaFisica;
import jdev.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase{


	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadPessoaFisica() throws ExceptionLojaVirtualJava {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setNome("Arthursilvateste");
		pessoaJuridica.setEmail("Arthurteste@gmail.com");
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setTelefone("619830573");
		pessoaJuridica.setInscEstadual("3453453453453");
		pessoaJuridica.setInscMunicipal("435345345345");
		pessoaJuridica.setNomeFantasia("fdgdfsgfgd");
		pessoaJuridica.setRazaoSocial("fdghsrfghjdfgh");
		
		pessoaController.salvarPj(pessoaJuridica);
		
	}
	
}
