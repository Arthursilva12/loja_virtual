package jdev.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CriarWebHook implements Serializable {

	private static final long serialVersionUID = 1L;

	private String url;
	
	private List<String> eventtypes = new ArrayList<String>();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getEventtypes() {
		return eventtypes;
	}

	public void setEventtypes(List<String> eventtypes) {
		this.eventtypes = eventtypes;
	}
	
}
