package jdev.lojavirtual.enums;

public enum TipoPessoa {

	JURIDICA("jurídica"),
	JURIDICA_FORNECEDOR("jurídica_fornecedor"),
	FISICA("fisica");
	
	private  String descricao;
	
	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}
