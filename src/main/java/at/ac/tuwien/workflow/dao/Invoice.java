package at.ac.tuwien.workflow.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import at.ac.tuwien.workflow.currencyconverter.CurrencyConverter;

public class Invoice implements Serializable {

	private static final long serialVersionUID = -7302148911238822832L;
	private List<Meal> meals = new ArrayList<Meal>();
	private Date invoiceDate;
	private double totalAmount;
	private String totalAmountCurrency;
	private double rateOfInvoiceDate;
	private String companyName;
	
	public Invoice(List<Meal> meals, Date invoiceDate, String totalAmountCurrency, String companyName) {
		super();
		setMeals(meals);
		this.invoiceDate = invoiceDate;
		this.totalAmountCurrency = totalAmountCurrency;
		this.companyName = companyName;
		
		//fork currency rate difference with random deviation
		double d = 0.0;
		do {
		    Random r = new Random(); 
		    d = -0.01 + r.nextDouble() * 0.02; 
		} while (!(d >= -0.01 && d <= 0.01));
		
		rateOfInvoiceDate = (1/ new CurrencyConverter().getRate(totalAmountCurrency)) * (1-d);		
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
