package jdev.lojavirtual.model.dto;

import java.io.Serializable;

public class CobrancaJunoApi implements Serializable {

	private static final long serialVersionUID = 1L;

	private ChargeDTO charge = new ChargeDTO();
	
	private Billing billing = new Billing();

	public ChargeDTO getCharge() {
		return charge;
	}

	public void setCharge(ChargeDTO charge) {
		this.charge = charge;
	}

	public Billing getBilling() {
		return billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}
	
	
}
