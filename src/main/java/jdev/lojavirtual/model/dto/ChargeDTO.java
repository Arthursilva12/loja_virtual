
package jdev.lojavirtual.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChargeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pixKey;
	private boolean pixIncludeInagen = true;
	private String description;
	private List<String> references = new ArrayList<String>();
	private Float amount;
	private String dueDate;// data vencimento
	private Integer installments;// parcelas
	private Integer maxDueDate;
	private BigDecimal fine;// multa pagamento pós vencimento
	private BigDecimal interest;// juros ao mês
	private List<String> paymentTypes = new ArrayList<String>();

	public String getPixKey() {
		return pixKey;
	}

	public void setPixKey(String pixKey) {
		this.pixKey = pixKey;
	}

	public boolean isPixIncludeInagen() {
		return pixIncludeInagen;
	}

	public void setPixIncludeInagen(boolean pixIncludeInagen) {
		this.pixIncludeInagen = pixIncludeInagen;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Integer getInstallments() {
		return installments;
	}

	public void setInstallments(Integer installments) {
		this.installments = installments;
	}

	public Integer getMaxDueDate() {
		return maxDueDate;
	}

	public void setMaxDueDate(Integer maxDueDate) {
		this.maxDueDate = maxDueDate;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public List<String> getPaymentTypes() {
		return paymentTypes;
	}

	public void setPaymentTypes(List<String> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

}
