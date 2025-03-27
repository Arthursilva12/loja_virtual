package jdev.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jdev.lojavirtual.model.PessoaFisica;
import jdev.lojavirtual.model.PessoaJuridica;
import jdev.lojavirtual.model.Usuario;
import jdev.lojavirtual.repository.PessoaFisicaRepository;
import jdev.lojavirtual.repository.PessoaRepository;
import jdev.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {
		
		for (int i = 0; i < juridica.getEnderecos().size(); i++) {
			juridica.getEnderecos().get(i).setPessoa(juridica); 
			juridica.getEnderecos().get(i).setEmpresa(juridica);
		}
		  
		juridica = pessoaRepository.save(juridica);
		// verifica usuário
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());
		
		if(usuarioPj == null) {//verifica restrição acesso
			String constraint = usuarioRepository.consultaConstraintAcesso();
			
			if(constraint != null) {// remove a restrição caso tenha
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint "+ constraint +"; commit;");
			}
			
			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(juridica);
			usuarioPj.setPessoa(juridica);
			usuarioPj.setLogin(juridica.getEmail());

			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPj.setSenha(senhaCript);
 			usuarioPj = usuarioRepository.save(usuarioPj);

			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			
			StringBuilder mensagemHtml = new StringBuilder();
			
			mensagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
			mensagemHtml.append("<b>Login: </b>"+juridica.getEmail()+"<br/>");
			mensagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
			mensagemHtml.append("Obrigado!");
			
			try {
				serviceSendEmail.enviarEmailHtml("Acesso gerado para loja virtual", mensagemHtml.toString(), juridica.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
 		}
		
		return juridica;
	}

	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		
		for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica); 
//			pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
		}
		  
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
		// verifica usuário
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());
		
		if(usuarioPj == null) {//verifica restrição acesso
			String constraint = usuarioRepository.consultaConstraintAcesso();
			
			if(constraint != null) {// remove a restrição caso tenha
				jdbcTemplate.execute("begin; alter table usuario_acesso drop constraint "+ constraint +"; commit;");
			}
			
			usuarioPj = new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(pessoaFisica.getEmpresa());
			usuarioPj.setPessoa(pessoaFisica);
			usuarioPj.setLogin(pessoaFisica.getEmail());

			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPj.setSenha(senhaCript);
 			usuarioPj = usuarioRepository.save(usuarioPj);

			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			
			StringBuilder mensagemHtml = new StringBuilder();
			
			mensagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b><br/>");
			mensagemHtml.append("<b>Login: </b>"+pessoaFisica.getEmail()+"<br/>");
			mensagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
			mensagemHtml.append("Obrigado!");
			
			try {
				serviceSendEmail.enviarEmailHtml("Acesso gerado para loja virtual", mensagemHtml.toString(), pessoaFisica.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
 		}
		
		return pessoaFisica;
	}
}
