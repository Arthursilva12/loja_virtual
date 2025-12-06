package jdev.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Objeto principal recebimento api juno boleto pix - webhook
public class DataNotificacaoJunoPagamentoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String eventId;
	private String eventType;
	private String timestemp;

	private List<AttibutesNotificacaoPagaApiJunoDto> data = new ArrayList<AttibutesNotificacaoPagaApiJunoDto>();

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getTimestemp() {
		return timestemp;
	}

	public void setTimestemp(String timestemp) {
		this.timestemp = timestemp;
	}

	public List<AttibutesNotificacaoPagaApiJunoDto> getData() {
		return data;
	}

	public void setData(List<AttibutesNotificacaoPagaApiJunoDto> data) {
		this.data = data;
	}

}
