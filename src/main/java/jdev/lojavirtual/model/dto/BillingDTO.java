package jdev.lojavirtual.model.dto;

import java.io.Serializable;

public class BillingDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean free;
	private boolean dataBase;

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public boolean isDataBase() {
		return dataBase;
	}

	public void setDataBase(boolean dataBase) {
		this.dataBase = dataBase;
	}

}
