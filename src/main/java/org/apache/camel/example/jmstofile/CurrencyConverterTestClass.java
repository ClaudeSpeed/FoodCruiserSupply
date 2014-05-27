package org.apache.camel.example.jmstofile;

import java.util.Date;


public class CurrencyConverterTestClass {

	
	
	public static void main(String[] args) {
		
		//Purchaselist for 400 persons
		Meal carbonara = new Meal("Spaghetti à la Carbonara",true);
		carbonara.addIngredient(new Ingredient("Spaghetti",60,"kg",300));
		carbonara.addIngredient(new Ingredient("Ham",30,"kg",1000));
		carbonara.addIngredient(new Ingredient("Cream",10,"l",200));
		carbonara.addIngredient(new Ingredient("Eggs",600,"units",500));
		carbonara.addIngredient(new Ingredient("Parmesan",10,"kg",650));
		
		PurchaseList purchList = new PurchaseList();
		purchList.addMeal(carbonara);
		
		//We get an invoice from the local retailer including the asked ingredients (incl. price),
		//invoiceDate and invoiceCurrency (here: Fiji Dollars).
		Invoice invoice = new Invoice(purchList.getMeals(), new Date(), "FJD");
		System.out.println("Invoice Total Amount: " + invoice.getTotalAmount() + " " + invoice.getTotalAmountCurrency());
		
		//The invoice will get paid by us after some time and we create a report for the accountancy,
		//including the settled invoice. The settle() saves the date of payment and the actual exchange rate.
		AccountancyReport report = new AccountancyReport(invoice);
		report.settle();	
		
		//The accountancy converts the total amount of the invoice into default currency and 
		//adds it to the report.
		CurrencyConverter converter = new CurrencyConverter();
		converter.convertCurrency(report);
		System.out.println("Converted Total Amount: " + report.getConvertedTotalAmount() + " " + converter.DEFAULT_CURRENCY);
		
		//Then the accountancy calculates the exchangeGain between the date of invoice and the
		//day of payment. (method fives 1 % gain for testing purposes)
		report.calculateExchangeProfit();
		System.out.println("Exchange Profit: " + report.getExchangeProfit() + " " + converter.DEFAULT_CURRENCY);
		
		//Then the report gets filed - TODO
	}

}
