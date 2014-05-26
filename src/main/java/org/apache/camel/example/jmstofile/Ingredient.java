package org.apache.camel.example.jmstofile;

public class Ingredient {
	
	public String Name = "";
	private int Quantity = 0;
	private String Unit = "";
	private int Price = 0;
	private String PriceCurrency = "";
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public String getPriceCurrency() {
		return PriceCurrency;
	}
	public void setPriceCurrency(String priceCurrency) {
		PriceCurrency = priceCurrency;
	}
}
