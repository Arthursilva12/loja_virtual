package jdev.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.Date;

public class SimeiDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean optante;
	private Date data_opcao;
	private Date data_exclusao;
	private Date ultima_atualizacao;

	public boolean isOptante() {
		return optante;
	}

	public void setOptante(boolean optante) {
		this.optante = optante;
	}

	public Date getData_opcao() {
		return data_opcao;
	}

	public void setData_opcao(Date data_opcao) {
		this.data_opcao = data_opcao;
	}

	public Date getData_exclusao() {
		return data_exclusao;
	}

	public void setData_exclusao(Date data_exclusao) {
		this.data_exclusao = data_exclusao;
	}

	public Date getUltima_atualizacao() {
		return ultima_atualizacao;
	}

	public void setUltima_atualizacao(Date ultima_atualizacao) {
		this.ultima_atualizacao = ultima_atualizacao;
	}

}
