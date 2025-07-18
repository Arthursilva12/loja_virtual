package jdev.lojavirtual.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class ObjetoRelatorioStatusCompraDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "Informe a data inicial")
	private String dataInicial;
	
	@NotEmpty(message = "Informe a data final")
	private String dataFinal ="";
	
	private String codigoProduto ="";
	private String nomeProduto ="";
	private String emailCliente ="";
	private String foneCliente ="";
	private String valorVendaProduto ="";
	private String codigoCodigoCliente ="";
	private String nomeCliente ="";
	private String qtdEstoque;
	private String codigoVenda ="";
	private String statusVenda ="";
	
	
	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public String getFoneCliente() {
		return foneCliente;
	}

	public void setFoneCliente(String foneCliente) {
		this.foneCliente = foneCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCodigoVenda() {
		return codigoVenda;
	}

	public void setCodigoVenda(String codigoVenda) {
		this.codigoVenda = codigoVenda;
	}

	public String getStatusVenda() {
		return statusVenda;
	}

	public void setStatusVenda(String statusVenda) {
		this.statusVenda = statusVenda;
	}

	public String getCodigoCodigoCliente() {
		return codigoCodigoCliente;
	}

	public void setCodigoCodigoCliente(String codigoCodigoCliente) {
		this.codigoCodigoCliente = codigoCodigoCliente;
	}

	public String getQtdEstoque() {
		return qtdEstoque;
	}

	public void setQtdEstoque(String qtdEstoque) {
		this.qtdEstoque = qtdEstoque;
	}

	public String getValorVendaProduto() {
		return valorVendaProduto;
	}

	public void setValorVendaProduto(String valorVendaProduto) {
		this.valorVendaProduto = valorVendaProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}


	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

}
