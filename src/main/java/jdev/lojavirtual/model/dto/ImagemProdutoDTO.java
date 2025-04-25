package jdev.lojavirtual.model.dto;

import java.io.Serializable;

public class ImagemProdutoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String imagem_original;

	private String imagem_miniatura;

	private Long produto;

	private Long empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImagem_original() {
		return imagem_original;
	}

	public void setImagem_original(String imagem_original) {
		this.imagem_original = imagem_original;
	}

	public String getImagem_miniatura() {
		return imagem_miniatura;
	}

	public void setImagem_miniatura(String imagem_miniatura) {
		this.imagem_miniatura = imagem_miniatura;
	}

	public Long getProduto() {
		return produto;
	}

	public void setProduto(Long produto) {
		this.produto = produto;
	}

	public Long getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Long empresa) {
		this.empresa = empresa;
	}

}
