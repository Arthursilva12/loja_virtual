package jdev.lojavirtual.model.dto;

import java.io.Serializable;

public class PayerNotificacaoApiJunoPaga implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String document;
	private String id;
	
	private AddressPayerPagamentoJuno addres = new AddressPayerPagamentoJuno();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AddressPayerPagamentoJuno getAddres() {
		return addres;
	}

	public void setAddres(AddressPayerPagamentoJuno addres) {
		this.addres = addres;
	}
	
}
