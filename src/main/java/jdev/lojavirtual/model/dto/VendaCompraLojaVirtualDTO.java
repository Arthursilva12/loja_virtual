package jdev.lojavirtual.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jdev.lojavirtual.model.Endereco;
import jdev.lojavirtual.model.Pessoa;

public class VendaCompraLojaVirtualDTO {

	private Long id;
	
	private BigDecimal valorTotal;
	
	private BigDecimal valorDesc;
	
	private BigDecimal valorFrete;
	
	private Pessoa pessoa;
	
	private Endereco cobranca;
	
	private Endereco entrega;
	
	private List<ItemVendaDTO> itemVendaLoja = new ArrayList<ItemVendaDTO>();
	
	public List<ItemVendaDTO> getItemVendaLoja() {
		return itemVendaLoja;
	}

	public void setItemVendaLoja(List<ItemVendaDTO> itemVendaLoja) {
		this.itemVendaLoja = itemVendaLoja;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public BigDecimal getValorDesc() {
		return valorDesc;
	}

	public void setValorDesc(BigDecimal valorDesc) {
		this.valorDesc = valorDesc;
	}

	public Endereco getCobranca() {
		return cobranca;
	}

	public void setCobranca(Endereco cobranca) {
		this.cobranca = cobranca;
	}

	public Endereco getEntrega() {
		return entrega;
	}

	public void setEntrega(Endereco entrega) {
		this.entrega = entrega;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}


}
