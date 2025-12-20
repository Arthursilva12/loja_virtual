package jdev.lojavirtual;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import jdev.lojavirtual.model.dto.CriarWebHook;
import jdev.lojavirtual.service.ServiceJunoBoleto;
import junit.framework.TestCase;

@Profile("teste")
@SpringBootTest(classes = LojaVirtualApplication.class)
public class TesteJunoBoleto extends TestCase {

	private ServiceJunoBoleto serviceJunoBoleto;
	
	public void testeCaseCriarWebhook () throws Exception {
		
		CriarWebHook criarWebHook = new CriarWebHook();
		criarWebHook.setUrl("https://lojavirtualmentoria-env.eba-bijtuvkg.sa-east-1.elasticbeanstalk.com/loja_virtual_mentoria/requisicaojunoboleto/notificacaoapiv2");
		
		criarWebHook.getEventtypes().add("PAYMENT_NOTIFICATION");
		criarWebHook.getEventtypes().add("BILL_PAYMENT_STATUS_CHANGED");
		
		String retorno = serviceJunoBoleto.criarWebHook(criarWebHook);
		
		System.out.println(retorno);

	}
	
}
