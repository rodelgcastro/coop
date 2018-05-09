package org.coop.api.model.loan;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payment_schedules")
public class PaymentSchedule {

	@Id
	private String id;
	private LocalDate dueDate;
	private BigDecimal amountPayable;
	private BigDecimal actualPayable;
	private BigDecimal payment;
	private LocalDate paymentDate;
	private BigDecimal balance;
	private BigDecimal latePaymentCharge;

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(BigDecimal amountPayable) {
		this.amountPayable = amountPayable;
	}

	public BigDecimal getActualPayable() {
		return actualPayable;
	}

	public void setActualPayable(BigDecimal actualPayable) {
		this.actualPayable = actualPayable;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getLatePaymentCharge() {
		return latePaymentCharge;
	}

	public void setLatePaymentCharge(BigDecimal latePaymentCharge) {
		this.latePaymentCharge = latePaymentCharge;
	}

}
