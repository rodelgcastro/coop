package org.coop.api.services;

import static org.coop.api.constants.LoanConstants.FIFTEEN;
import static org.coop.api.constants.LoanConstants.ONE;
import static org.coop.api.constants.LoanConstants.PHP_ZERO;
import static org.coop.api.constants.LoanConstants.SEVEN;
import static org.coop.api.constants.LoanConstants.TWENTY_THREE;
import static org.coop.api.constants.LoanConstants.TWO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.coop.api.model.loan.PaymentMode;
import org.coop.api.model.loan.PaymentSchedule;
import org.coop.api.model.loan.Quotation;
import org.springframework.stereotype.Service;

@Service
public class QuotationService {

	public Quotation compute(Quotation quotation) {
		Quotation result = new Quotation(quotation.getLoanAmount(), quotation.getTerm(), quotation.getRate(),
				quotation.getLpcRate(), quotation.getMode(), quotation.getReleaseDate());

		// LocalDate today = LocalDate.now();
		// result.setUpdateDate(today);

		BigDecimal interestAmount = quotation.getLoanAmount().multiply(quotation.getTerm())
				.multiply(quotation.getRate()).setScale(2, RoundingMode.HALF_DOWN);
		result.setInterestAmount(interestAmount);

		BigDecimal loanPayable = quotation.getLoanAmount().add(interestAmount);
		result.setLoanPayable(loanPayable);

		BigDecimal totalPayable = loanPayable;

		List<PaymentSchedule> paymentSchedules = new ArrayList<>();

		LocalDate firstPaymentDate = getFirstPaymentDate(quotation.getReleaseDate());

		int paymentScheduleCount = quotation.getTerm().intValue() * TWO;

		// BigDecimal previousBalance = PHP_ZERO;

		PaymentSchedule previousSchedule = new PaymentSchedule();
		previousSchedule.setBalance(PHP_ZERO);
		previousSchedule.setLatePaymentCharge(PHP_ZERO);
		for (int i = 1; i <= paymentScheduleCount; i++) {
			PaymentSchedule paymentSchedule = new PaymentSchedule();

			// GET DUE DATE
			LocalDate dueDate = getNthDueDate(firstPaymentDate, i);
			paymentSchedule.setDueDate(dueDate);

			// GET AMOUNT PAYABLE
			BigDecimal paymentCount = new BigDecimal(paymentScheduleCount);
			BigDecimal amountPayable = loanPayable.divide(paymentCount, TWO, RoundingMode.HALF_DOWN);
			if (i == paymentScheduleCount) {
				BigDecimal totalPayed = amountPayable.multiply(paymentCount.subtract(BigDecimal.ONE));
				amountPayable = loanPayable.subtract(totalPayed);
			}
			paymentSchedule.setAmountPayable(amountPayable);

			// GET ACTUAL PAYABLE
			BigDecimal actualPayable = amountPayable.add(previousSchedule.getBalance())
					.add(previousSchedule.getLatePaymentCharge());
			paymentSchedule.setActualPayable(actualPayable);

			// GET PAYMENT (NO PAYMENT ON QUOTATION)
			paymentSchedule.setPayment(PHP_ZERO);

			// GET PAYMENT DATE
			if (quotation.getMode().equals(PaymentMode.SEMI_MONTHLY)) {
				paymentSchedule.setPaymentDate(dueDate);

			} else if (quotation.getMode().equals(PaymentMode.MONTHLY)) {
				if (isOdd(i)) {
					paymentSchedule.setPaymentDate(getNthDueDate(firstPaymentDate, i + 1));
				} else {
					paymentSchedule.setPaymentDate(getNthDueDate(firstPaymentDate, i));
				}

			} else if (quotation.getMode().equals(PaymentMode.ONE_TIME_PAYMENT)) {
				paymentSchedule.setPaymentDate(getNthDueDate(firstPaymentDate, paymentScheduleCount));
			}

			// GET BALANCE
			paymentSchedule.setBalance(actualPayable.subtract(paymentSchedule.getPayment()));

			// GET LATE PAYMENT CHARGE
			if (paymentSchedule.getPaymentDate().isAfter(paymentSchedule.getDueDate().plusDays(TWO))) {
				BigDecimal latePaymentCharge = paymentSchedule.getBalance().multiply(quotation.getLpcRate())
						.setScale(TWO, RoundingMode.HALF_DOWN);
				paymentSchedule.setLatePaymentCharge(latePaymentCharge);
			} else {
				paymentSchedule.setLatePaymentCharge(PHP_ZERO);
			}

			paymentSchedules.add(paymentSchedule);
			previousSchedule = paymentSchedule;
		}
		result.setPaymentSchedules(paymentSchedules);

		BigDecimal totalLPC = paymentSchedules.stream().map(PaymentSchedule::getLatePaymentCharge)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		result.setTotalPayable(totalPayable.add(totalLPC));

		return result;
	}

	private LocalDate getFirstPaymentDate(LocalDate releaseDate) {
		int year = releaseDate.getYear();
		int month = releaseDate.getMonthValue();

		if (isBeforeDaySeven(releaseDate)) {
			return LocalDate.of(year, month, FIFTEEN);

		} else if (isNot(isBeforeDaySeven(releaseDate)) && isNot(isAfterDayTwentyThree(releaseDate))) {
			return LocalDate.of(year, month, releaseDate.lengthOfMonth());

		} else if (isAfterDayTwentyThree(releaseDate)) {
			int nextMonth = month + ONE;
			return LocalDate.of(year, nextMonth, nextMonth);

		}

		return null;
	}

	private LocalDate getNthDueDate(LocalDate firstPaymentDate, int nthPayment) {

		LocalDate dueDate = null;
		if (firstPaymentDate.getDayOfMonth() == FIFTEEN && isOdd(nthPayment)) {
			dueDate = firstPaymentDate.plusMonths((nthPayment - ONE) / TWO);
		} else if (firstPaymentDate.getDayOfMonth() == FIFTEEN && isEven(nthPayment)) {
			dueDate = firstPaymentDate.minusDays(FIFTEEN).plusMonths(nthPayment / TWO)
					.with(TemporalAdjusters.lastDayOfMonth());
		} else if (firstPaymentDate.getDayOfMonth() != FIFTEEN && isOdd(nthPayment)) {
			dueDate = firstPaymentDate.plusDays(FIFTEEN).plusMonths((nthPayment - ONE) / TWO).minusDays(FIFTEEN);
		} else if (firstPaymentDate.getDayOfMonth() != FIFTEEN && isEven(nthPayment)) {
			dueDate = firstPaymentDate.plusDays(FIFTEEN).minusDays(FIFTEEN).plusMonths(nthPayment / TWO)
					.with(TemporalAdjusters.lastDayOfMonth());
			dueDate = dueDate.minusDays(dueDate.lengthOfMonth() - FIFTEEN);
		}
		return dueDate;

	}

	private boolean isBeforeDaySeven(LocalDate releaseDate) {
		LocalDate referenceDate = LocalDate.of(releaseDate.getYear(), releaseDate.getMonth(), SEVEN);
		return releaseDate.isBefore(referenceDate);
	}

	private boolean isAfterDayTwentyThree(LocalDate releaseDate) {
		LocalDate referenceDate = LocalDate.of(releaseDate.getYear(), releaseDate.getMonth(), TWENTY_THREE);
		return releaseDate.isAfter(referenceDate);
	}

	public boolean isNot(boolean check) {
		return !check;
	}

	public boolean isEven(int number) {
		return number % 2 == 0;
	}

	public boolean isOdd(int number) {
		return !isEven(number);
	}

}
