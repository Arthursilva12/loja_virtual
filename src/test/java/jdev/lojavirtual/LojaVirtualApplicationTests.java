package jdev.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jdev.lojavirtual.controller.AcessoController;
import jdev.lojavirtual.model.Acesso;

@SpringBootTest(classes = LojaVirtualApplication.class)
class LojaVirtualApplicationTests {

	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void testeCadastraAcesso() {
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_AUXILIAR");
		
		acessoController.salvarAcesso(acesso);
	}

}
