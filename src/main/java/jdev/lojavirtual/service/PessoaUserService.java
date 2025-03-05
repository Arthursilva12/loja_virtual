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
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {
		
		juridica = pessoaRepository.save(juridica);
		
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());
		
		if(usuarioPj == null) {
			String constraint = usuarioRepository.consultaConstraintAcesso();
			
			if(constraint != null) {
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
 		}
		
		return juridica;
	}
}
