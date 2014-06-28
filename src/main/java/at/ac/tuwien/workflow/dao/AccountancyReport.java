package at.ac.tuwien.workflow.dao;

import java.io.Serializable;
import java.util.Date;

import at.ac.tuwien.workflow.currencyconverter.CurrencyConverter;

public class AccountancyReport implements Serializable {
	
	private static final long serialVersionUID = 6301110483090646094L;
	private Invoice invoice;
	private Date paymentDate;
	private double rateOfPaymentDate;
	private double convertedTotalAmount;
	private String convertedTotalAmountCurrency;
	private double exchangeProfit;
	
	public AccountancyReport(Invoice invoice) {
		super();
		this.invoice = invoice;
	}
	
	public void settleInvoice() {
		paymentDate = new Date();
		rateOfPaymentDate = new CurrencyConverter().getRate(invoice.getTotalAmountCurrency());
	}
	
	public void calculateExchangeProfit() {
		exchangeProfit = convertedTotalAmount - (invoice.getTotalAmount() * invoice.getRateOfInvoiceDate());
	}

	public String getConvertedTotalAmountCurrency() {
		return convertedTotalAmountCurrency;
	}

	public void setConvertedTotalAmountCurrency(String convertedTotalAmountCurrency) {
		this.convertedTotalAmountCurrency = convertedTotalAmountCurrency;
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
