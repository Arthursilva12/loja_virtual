package jdev.lojavirtual.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TemporalType;

@Entity
@Table(name = "access_token_junoapi")
@SequenceGenerator(name = "access_token_junoapi", sequenceName = "seq_access_token_junoapi",allocationSize = 1, initialValue = 1)
public class AccessTokenJunoAPI implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@javax.persistence.Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_access_token_junoapi")
	private Long id;
	
	@Column(columnDefinition = "text")
	private String Access_token;
	
	private String token_type;
	
	private String expires_in;
	
	private String scope;
	
	private String user_name;
	
	private String jti;
	
	private String token_acesso;
	
	@Column(updatable = false)
	@javax.persistence.Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro = Calendar.getInstance().getTime();

	public boolean expirado() {
		Date dataAtual = Calendar.getInstance().getTime();
		Long tempo = dataAtual.getTime() - this.dataCadastro.getTime();// Tempo entre as datas
		Long minutos = (tempo / 1000) / 60;// Diferença de minutos entre datas e horas inicial e final 
		
		if (minutos.intValue() > 50) {
			return true;
		}
		
		return false;
	}
	
	public String getToken_acesso() {
		return token_acesso;
	}

	public void setToken_acesso(String token_acesso) {
		this.token_acesso = token_acesso;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccess_token() {
		return Access_token;
	}

	public void setAccess_token(String access_token) {
		Access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
}
