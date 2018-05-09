package org.coop.api.model.loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Quotation {

	private BigDecimal loanAmount;
	private BigDecimal term;
	private BigDecimal rate;
	private BigDecimal lpcRate;
	private PaymentMode mode;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate releaseDate;
	private BigDecimal interestAmount;
	private BigDecimal loanPayable;
	private List<PaymentSchedule> paymentSchedules;
	private BigDecimal totalPayable;

	public Quotation() {
		super();
	}

	public Quotation(BigDecimal loanAmount, BigDecimal term, BigDecimal rate, BigDecimal lpcRate, PaymentMode mode,
			LocalDate releaseDate) {
		super();
		this.loanAmount = loanAmount;
		this.term = term;
		this.rate = rate;
		this.lpcRate = lpcRate;
		this.mode = mode;
		this.releaseDate = releaseDate;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public BigDecimal getTerm() {
		return term;
	}

	public void setTerm(BigDecimal term) {
		this.term = term;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getLpcRate() {
		return lpcRate;
	}

	public void setLpcRate(BigDecimal lpcRate) {
		this.lpcRate = lpcRate;
	}

	public PaymentMode getMode() {
		return mode;
	}

	public void setMode(PaymentMode mode) {
		this.mode = mode;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public BigDecimal getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public BigDecimal getLoanPayable() {
		return loanPayable;
	}

	public void setLoanPayable(BigDecimal loanPayable) {
		this.loanPayable = loanPayable;
	}

	public List<PaymentSchedule> getPaymentSchedules() {
		if (paymentSchedules == null) {
			paymentSchedules = new ArrayList<>();
		}
		return paymentSchedules;
	}

	public void setPaymentSchedules(List<PaymentSchedule> paymentSchedules) {
		this.paymentSchedules = paymentSchedules;
	}

	public BigDecimal getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(BigDecimal totalPayable) {
		this.totalPayable = totalPayable;
	}
}
