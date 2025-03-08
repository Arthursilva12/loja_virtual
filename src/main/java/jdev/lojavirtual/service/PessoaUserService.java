package jdev.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jdev.lojavirtual.model.PessoaJuridica;
import jdev.lojavirtual.model.Usuario;
import jdev.lojavirtual.repository.PessoaRepository;
import jdev.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
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
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint "+ constraint +"; commit;");
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

			usuarioRepository.insereAcessoUserPj(usuarioPj.getId());
			
			StringBuilder mensagemHtml = new StringBuilder();
			
			mensagemHtml.append("<b>Segue abaixo sues dados de acesso para a loja virtual<b>");
			mensagemHtml.append("<b>Login: </b>"+juridica.getEmail()+"</b><b/>");
			mensagemHtml.append("<b>Senha: </b>").append(senha).append("</b><b/>");
			mensagemHtml.append("Obrigado!");
			
			try {
				serviceSendEmail.enviarEmailHtml("Acesso gerado para loja virtual", mensagemHtml.toString(), juridica.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
 		}
		
		return juridica;
	}
}
