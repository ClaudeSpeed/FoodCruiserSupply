package at.ac.tuwien.workflow.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ac.tuwien.workflow.currencyconverter.CurrencyConverter;

public class Invoice implements Serializable {

	private static final long serialVersionUID = -7302148911238822832L;
	private List<Meal> meals = new ArrayList<Meal>();
	private Date invoiceDate;
	private double totalAmount;
	private String totalAmountCurrency;
	private double rateOfInvoiceDate;
	
	public Invoice(List<Meal> meals, Date invoiceDate, String totalAmountCurrency) {
		super();
		setMeals(meals);
		this.invoiceDate = invoiceDate;
		this.totalAmountCurrency = totalAmountCurrency;
		rateOfInvoiceDate = (1/ new CurrencyConverter().getRate(totalAmountCurrency));		
	}
	
	public double getRateOfInvoiceDate() {
		return rateOfInvoiceDate;
	}


	public void setRateOfInvoiceDate(double rateOfInvoiceDate) {
		this.rateOfInvoiceDate = rateOfInvoiceDate;
	}


	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
		for (Meal m : meals) {
			for (Ingredient i : m.getIngredients()) totalAmount += i.getPrice();
		}
	}
	
	public void addMeal(Meal meal){
		this.meals.add(meal);
		for (Ingredient i : meal.getIngredients()) totalAmount += i.getPrice();
	}
	
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTotalAmountCurrency() {
		return totalAmountCurrency;
	}

	public void setTotalAmountCurrency(String totalAmountCurrency) {
		this.totalAmountCurrency = totalAmountCurrency;
	}
	
}
