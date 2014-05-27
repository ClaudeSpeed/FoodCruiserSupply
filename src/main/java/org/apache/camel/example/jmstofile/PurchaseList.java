package org.apache.camel.example.jmstofile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseList {

	private List<Meal> meals = new ArrayList<Meal>();
	private Date menueDate;
	private String menueType;
	
	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
	}
	
	public void addMeal(Meal meal){
		this.meals.add(meal);
	}

	public Date getMenueDate() {
		return menueDate;
	}

	public void setMenueDate(Date menueDate) {
		this.menueDate = menueDate;
	}

	public String getMenueType() {
		return menueType;
	}

	public void setMenueType(String menueType) {
		this.menueType = menueType;
	}
	
}
