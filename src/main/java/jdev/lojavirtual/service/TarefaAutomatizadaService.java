package jdev.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jdev.lojavirtual.model.Usuario;
import jdev.lojavirtual.repository.UsuarioRepository;

@Component
@Service
public class TarefaAutomatizadaService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	 
	@Scheduled(initialDelay = 2000, fixedDelay = 86400000)// Roda a cada 24h
//	@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo")// roda todo dia as 11h da manhã no horario de brasilia 
	public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
		List<Usuario> usuarios = repository.usuarioSenhaVencida();
		
		for (Usuario usuario : usuarios) {
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
			msg.append("Está na hora de trocar sua senha, já passou 90 dias de validade.").append("<br/>");
			msg.append("Troque sua senha da loja virtual.");
			
			serviceSendEmail.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());
			
			Thread.sleep(3000);
		}
	}
	
}
