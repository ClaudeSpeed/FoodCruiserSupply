package org.apache.camel.example.jmstofile;

public class Ingredient {
	
	public String Name = "";
	private double Quantity = 0;
	private String Unit = "";
	private double Price = 0;
	
	public Ingredient(String name, int quantity, String unit, int price) {
		super();
		Name = name;
		Quantity = quantity;
		Unit = unit;
		Price = price;
	}
	
	public Ingredient() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public double getQuantity() {
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
	public double getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}

}
