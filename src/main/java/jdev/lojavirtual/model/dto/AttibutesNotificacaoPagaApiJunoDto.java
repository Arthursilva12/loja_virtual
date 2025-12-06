package jdev.lojavirtual.model.dto;

import java.io.Serializable;

public class AttibutesNotificacaoPagaApiJunoDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String entityId;
	private String entityType;
	
	private AtributesNotificacaoPagaApiJunoDto attributes = new AtributesNotificacaoPagaApiJunoDto();

	public String getEntityId() {
		return entityId;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public AtributesNotificacaoPagaApiJunoDto getAttributes() {
		return attributes;
	}

	public void setAttributes(AtributesNotificacaoPagaApiJunoDto attributes) {
		this.attributes = attributes;
	}
	
	
}
