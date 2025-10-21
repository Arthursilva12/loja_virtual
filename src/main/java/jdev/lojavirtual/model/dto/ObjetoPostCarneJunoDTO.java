package jdev.lojavirtual.model.dto;

import java.io.Serializable;

public class ObjetoPostCarneJunoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Descrição da cobrança
	private String description;
	
	// Nome do comprador/cliente
	private String payerName;
	
	// Fone do clinete/comprador
	private String payerPhone;
	
	// valor da compra ou parcela
	private String totalAmount;
	
	// Quantidade de parcelas
	private String installments;
	
	// Referencia para produto da loja ou codigo do produto
	private String refence;

	private String payerCpfCnpj;
	
	private String email;
	
	private Long idVenda;
	
	public Long getIdVenda() {
		return idVenda;
	}
	
	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPayerCpfCnpj() {
		return payerCpfCnpj;
	}
	
	public void setPayerCpfCnpj(String payerCpfCnpj) {
		this.payerCpfCnpj = payerCpfCnpj;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayerPhone() {
		return payerPhone;
	}

	public void setPayerPhone(String payerPhone) {
		this.payerPhone = payerPhone;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInstallments() {
		return installments;
	}

	public void setInstallments(String installments) {
		this.installments = installments;
	}

	public String getRefence() {
		return refence;
	}

	public void setRefence(String refence) {
		this.refence = refence;
	}

}
