package at.ac.tuwien.workflow.dao;

import java.io.Serializable;

public class Ingredient implements Serializable {
	
	private static final long serialVersionUID = -7726285709864858431L;
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
	
	public Ingredient(String name, int quantity, String unit) {
		super();
		Name = name;
		Quantity = quantity;
		Unit = unit;
	}
	
	public Ingredient() {
		super();
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
