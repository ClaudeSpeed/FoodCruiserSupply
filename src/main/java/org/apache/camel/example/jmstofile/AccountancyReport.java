package org.apache.camel.example.jmstofile;

import java.util.Date;

public class AccountancyReport {
	
	private Invoice invoice;
	private Date paymentDate;
	private double rateOfPaymentDate;
	private double convertedTotalAmount;
	private double exchangeProfit;
	
	public AccountancyReport(Invoice invoice) {
		super();
		this.invoice = invoice;
	}
	
	public void settle() {
		paymentDate = new Date();
		rateOfPaymentDate = new CurrencyConverter().getRate(invoice.getTotalAmountCurrency());
	}
	
	//TODO 1 % gain for testing purposes
	public void calculateExchangeProfit() {
		exchangeProfit = convertedTotalAmount - (invoice.getTotalAmount() * invoice.getRateOfInvoiceDate() *0.99);
	}

	public double getRateOfPaymentDate() {
		return rateOfPaymentDate;
	}

	public void setRateOfPaymentDate(double rateOfPaymentDate) {
		this.rateOfPaymentDate = rateOfPaymentDate;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getConvertedTotalAmount() {
		return convertedTotalAmount;
	}

	public void setConvertedTotalAmount(double convertedTotalAmount) {
		this.convertedTotalAmount = convertedTotalAmount;
	}

	public double getExchangeProfit() {
		return exchangeProfit;
	}

	public void setExchangeProfit(double exchangeProfit) {
		this.exchangeProfit = exchangeProfit;
	}
	
}
